package cc.bitky.clusterdeviceplatform.server.server.repo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;
import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.db.work.bean.EmployeeItem;
import cc.bitky.clusterdeviceplatform.server.db.work.bean.StatusItem;
import cc.bitky.clusterdeviceplatform.server.server.utils.DeviceOutBoundDetect;
import cc.bitky.clusterdeviceplatform.server.web.spa.data.bean.DeviceStatusItem;
import io.netty.util.internal.ConcurrentSet;

@Repository
public class DeviceStatusRepository {
    /**
     * 设备状态暂存
     */
    private final AtomicReferenceArray<AtomicReferenceArray<StatusItem>> CHARGE_STATUS_STORAGE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
    private final AtomicReferenceArray<AtomicReferenceArray<StatusItem>> WORK_STATUS_STORAGE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
    /**
     * 员工引用缓存
     */
    private final AtomicReferenceArray<AtomicReferenceArray<EmployeeItem>> EMPLOYEE_LINKED_STORAGE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
    /**
     * 员工分部门缓存
     */
    private final ConcurrentHashMap<String, ConcurrentSet<EmployeeItem>> employeeItemSet = new ConcurrentHashMap<>();
    /**
     * 设备组最近通信时间
     */
    private final AtomicLongArray DEVICEGROUP_RECENT_COMM = new AtomicLongArray(DeviceSetting.MAX_GROUP_ID + 1);

    {
        for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
            AtomicReferenceArray<StatusItem> array1 = new AtomicReferenceArray<>(DeviceSetting.MAX_DEVICE_ID + 1);
            AtomicReferenceArray<StatusItem> array2 = new AtomicReferenceArray<>(DeviceSetting.MAX_DEVICE_ID + 1);
            AtomicReferenceArray<EmployeeItem> array3 = new AtomicReferenceArray<>(DeviceSetting.MAX_DEVICE_ID + 1);
            for (int j = 0; j <= DeviceSetting.MAX_DEVICE_ID; j++) {
                array1.set(j, new StatusItem());
                array2.set(j, new StatusItem());
            }
            CHARGE_STATUS_STORAGE.set(i, array1);
            WORK_STATUS_STORAGE.set(i, array2);
            EMPLOYEE_LINKED_STORAGE.set(i, array3);
        }
    }

    /**
     * 从服务器暂存库中，获取设备状态
     *
     * @param groupId  设备组号
     * @param deviceId 设备号
     * @return 指定设备的状态
     */
    private StatusItem getChargeStatus(int groupId, int deviceId) {
        return CHARGE_STATUS_STORAGE.get(groupId).get(deviceId);
    }

    private void setChargeStatus(int groupId, int deviceId, StatusItem item) {
        CHARGE_STATUS_STORAGE.get(groupId).set(deviceId, item);
    }

    private StatusItem getWorkStatus(int groupId, int deviceId) {
        return WORK_STATUS_STORAGE.get(groupId).get(deviceId);
    }

    private void setWorkStatus(int groupId, int deviceId, StatusItem item) {
        WORK_STATUS_STORAGE.get(groupId).set(deviceId, item);
    }

    /**
     * 获取设备组最近通信时刻的时间戳
     *
     * @param groupId 设备组 ID
     * @return 特定的时间戳
     */
    public long getDeviceGroupRecentCommTime(int groupId) {
        if (DeviceOutBoundDetect.detectGroup(groupId)) {
            return 0;
        }
        return DEVICEGROUP_RECENT_COMM.get(groupId);
    }

    /**
     * 更新设备组最近通信时刻的时间戳
     *
     * @param groupId 设备组 ID
     */
    private void updateDeviceGroupRecentCommTime(int groupId) {
        if (DeviceOutBoundDetect.detectGroup(groupId)) {
            return;
        }
        DEVICEGROUP_RECENT_COMM.set(groupId, System.currentTimeMillis());
    }

    /**
     * 从服务器缓存中获取设备状态
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @param type     状态类型
     * @return 状态 Item
     */
    public StatusItem getStatus(int groupId, int deviceId, MsgReplyDeviceStatus.Type type) {
        if (DeviceOutBoundDetect.detect(groupId, deviceId)) {
            return new StatusItem();
        }
        updateDeviceGroupRecentCommTime(groupId);
        if (type == MsgReplyDeviceStatus.Type.CHARGE) {
            return getChargeStatus(groupId, deviceId);
        } else {
            return getWorkStatus(groupId, deviceId);
        }
    }

    public void setStatus(int groupId, int deviceId, StatusItem item, MsgReplyDeviceStatus.Type type) {
        if (type == MsgReplyDeviceStatus.Type.CHARGE) {
            setChargeStatus(groupId, deviceId, item);
        } else {
            setWorkStatus(groupId, deviceId, item);
        }
    }


    /**
     * 清理所有员工的引用
     */
    public void clearEmployeeItemsRef() {
        employeeItemSet.clear();
        for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
            AtomicReferenceArray<EmployeeItem> array = new AtomicReferenceArray<>(DeviceSetting.MAX_DEVICE_ID + 1);
            EMPLOYEE_LINKED_STORAGE.set(i, array);
        }
    }


    /**
     * 添加员工对象的引用
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @param item     员工对象
     */
    public void addEmployeeItemsRef(int groupId, int deviceId, EmployeeItem item) {
        if (!employeeItemSet.containsKey(item.getDepartment())) {
            employeeItemSet.put(item.getDepartment(), new ConcurrentSet<>());
        }
        employeeItemSet.get(item.getDepartment()).add(item);
        EMPLOYEE_LINKED_STORAGE.get(groupId).set(deviceId, item);
    }

    /**
     * 获取设备状态引用集合「按设备组」
     *
     * @param groupId 设备组 ID
     * @return 设备状态引用集合
     */
    public List<DeviceStatusItem> getEmployeeItemsRefByCoordinate(int groupId) {
        AtomicReferenceArray<StatusItem> chargeItems = CHARGE_STATUS_STORAGE.get(groupId);
        AtomicReferenceArray<StatusItem> workItems = WORK_STATUS_STORAGE.get(groupId);
        List<DeviceStatusItem> itemList = new ArrayList<>(DeviceSetting.MAX_DEVICE_ID);
        for (int i = 1; i <= DeviceSetting.MAX_DEVICE_ID; i++) {
            itemList.add(new DeviceStatusItem(chargeItems.get(i).getStatus(), workItems.get(i).getStatus()));
        }
        return itemList;
    }

    /**
     * 获取设备状态引用集合「按部门」
     *
     * @param department 指定的部门
     * @return 设备状态引用集合
     */
    public List<DeviceStatusItem> getEmployeeItemsRefByDepartment(String department) {
        ConcurrentSet<EmployeeItem> map = employeeItemSet.get(department);
        List<DeviceStatusItem> itemList = new ArrayList<>(map.size());
        map.iterator().forEachRemaining(item ->
                itemList.add(new DeviceStatusItem(
                        getChargeStatus(item.getGroupId(), item.getDeviceId()).getStatus(),
                        getWorkStatus(item.getGroupId(), item.getDeviceId()).getStatus())));
        return itemList;
    }

    /**
     * 获取员工部门的集合
     *
     * @return 目标数合
     */
    public Map<String, List<DeviceStatusItem>> getDepartmentCategory() {
        Map<String, List<DeviceStatusItem>> map = new HashMap<>(employeeItemSet.size());
        employeeItemSet.forEach((department, set) -> {
            List<DeviceStatusItem> items = new ArrayList<>(set.size());
            set.iterator().forEachRemaining(item -> {
                items.add(new DeviceStatusItem(CHARGE_STATUS_STORAGE.get(item.getGroupId()).get(item.getDeviceId()).getStatus(),
                        WORK_STATUS_STORAGE.get(item.getGroupId()).get(item.getDeviceId()).getStatus()));
            });
            map.put(department, items);
        });
        return map;
    }
}
