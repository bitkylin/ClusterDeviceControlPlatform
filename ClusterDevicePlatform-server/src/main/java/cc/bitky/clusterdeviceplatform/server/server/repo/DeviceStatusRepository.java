package cc.bitky.clusterdeviceplatform.server.server.repo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyNormal;
import cc.bitky.clusterdeviceplatform.server.db.bean.Employee;
import cc.bitky.clusterdeviceplatform.server.server.repo.bean.DeviceItem;
import cc.bitky.clusterdeviceplatform.server.server.repo.bean.StatusItem;
import cc.bitky.clusterdeviceplatform.server.server.utils.DeviceOutBoundDetect;
import cc.bitky.clusterdeviceplatform.server.web.spa.data.bean.DeviceStatusItem;
import cc.bitky.clusterdeviceplatform.server.web.spa.tcp.bean.BaseMsgSending;
import cc.bitky.clusterdeviceplatform.server.web.spa.tcp.bean.DeviceGroupedMsgSending;
import cc.bitky.clusterdeviceplatform.server.web.spa.tcp.bean.DeviceItemMsgSending;
import cc.bitky.clusterdeviceplatform.server.web.spa.tcp.bean.MachineMsgSending;
import cc.bitky.clusterdeviceplatform.server.web.spa.tcp.bean.MsgSending;
import io.netty.util.internal.ConcurrentSet;

import static cc.bitky.clusterdeviceplatform.server.config.DeviceSetting.MAX_DEVICE_ID;
import static cc.bitky.clusterdeviceplatform.server.config.DeviceSetting.MAX_GROUP_ID;

/**
 * 设备状态的服务器缓存
 */
@Repository
public class DeviceStatusRepository {
    /**
     * 设备状态暂存
     */
    private final DeviceItem[][] DEVICE_ITEMS = new DeviceItem[MAX_GROUP_ID][MAX_DEVICE_ID];
    /**
     * 员工分部门缓存
     */
    private final ConcurrentHashMap<String, ConcurrentSet<Employee>> employeeItemSetByDepartment = new ConcurrentHashMap<>();

    {
        for (int i = 0; i < MAX_GROUP_ID; i++) {
            for (int j = 0; j < MAX_DEVICE_ID; j++) {
                DEVICE_ITEMS[i][j] = new DeviceItem(i + 1, j + 1);
            }
        }
    }

    /**
     * 从服务器暂存库中，获取设备的充电状态
     *
     * @param groupId  设备组号
     * @param deviceId 设备号
     * @return 指定设备的充电状态
     */
    private StatusItem getChargeStatus(int groupId, int deviceId) {
        return obtainDeviceItem(groupId, deviceId).obtainChargeStatus();
    }

    /**
     * 服务器暂存库中，保存设备的充电状态
     *
     * @param groupId  设备组号
     * @param deviceId 设备号
     * @param item     充电状态对象
     */
    private void setChargeStatus(int groupId, int deviceId, StatusItem item) {
        obtainDeviceItem(groupId, deviceId).saveChargeStatus(item);
    }

    /**
     * 从服务器暂存库中，获取设备的工作状态
     *
     * @param groupId  设备组号
     * @param deviceId 设备号
     * @return 指定设备的工作状态
     */
    private StatusItem getWorkStatus(int groupId, int deviceId) {
        return obtainDeviceItem(groupId, deviceId).obtainWorkStatus();
    }

    /**
     * 服务器暂存库中，保存设备的工作状态
     *
     * @param groupId  设备组号
     * @param deviceId 设备号
     * @param item     工作状态对象
     */
    private void setWorkStatus(int groupId, int deviceId, StatusItem item) {
        obtainDeviceItem(groupId, deviceId).saveWorkStatus(item);
    }

    /**
     * 服务器暂存库中，获取指定的设备
     *
     * @param groupId  设备组号
     * @param deviceId 设备号
     * @return 指定设备的对象
     */
    private DeviceItem obtainDeviceItem(int groupId, int deviceId) {
        return DEVICE_ITEMS[groupId - 1][deviceId - 1];
    }

    /**
     * 服务器暂存库中，获取指定的设备组
     *
     * @param groupId 设备组号
     * @return 指定设备组的对象
     */
    private DeviceItem[] obtainDeviceGroup(int groupId) {
        return DEVICE_ITEMS[groupId - 1];
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
        employeeItemSetByDepartment.clear();
        for (int i = 1; i <= MAX_GROUP_ID; i++) {
            for (int j = 1; j <= MAX_DEVICE_ID; j++) {
                DeviceItem deviceItem = obtainDeviceItem(i, j);
                deviceItem.saveEmployee(null);
            }
        }
    }

    /**
     * 添加员工对象的引用
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @param item     员工对象
     */
    public void addEmployeeItemsRef(int groupId, int deviceId, Employee item) {
        if (!employeeItemSetByDepartment.containsKey(item.getDepartment())) {
            employeeItemSetByDepartment.put(item.getDepartment(), new ConcurrentSet<>());
        }
        employeeItemSetByDepartment.get(item.getDepartment()).add(item);
        obtainDeviceItem(groupId, deviceId).saveEmployee(item);
    }

    /**
     * 获取设备状态引用集合「按设备组」
     *
     * @param groupId 设备组 ID
     * @return 设备状态引用集合
     */
    public List<DeviceStatusItem> getEmployeeItemsRefByCoordinate(int groupId) {
        List<DeviceStatusItem> itemList = new ArrayList<>(MAX_DEVICE_ID);
        for (int deviceId = 1; deviceId <= MAX_DEVICE_ID; deviceId++) {
            itemList.add(new DeviceStatusItem(getChargeStatus(groupId, deviceId).getStatus(), getWorkStatus(groupId, deviceId).getStatus()));
        }
        return itemList;
    }

    /**
     * 获取员工部门的集合
     *
     * @return 目标数合
     */
    public Map<String, List<DeviceStatusItem>> getDepartmentCategory() {
        Map<String, List<DeviceStatusItem>> map = new HashMap<>(employeeItemSetByDepartment.size());
        employeeItemSetByDepartment.forEach((department, set) -> {
            List<DeviceStatusItem> items = new ArrayList<>(set.size());
            set.iterator().forEachRemaining(item -> items.add(new DeviceStatusItem(
                    getChargeStatus(item.getGroupId(), item.getDeviceId()).getStatus(),
                    getWorkStatus(item.getGroupId(), item.getDeviceId()).getStatus())));
            map.put(department, items);
        });
        return map;
    }

    /**
     * 将待发送消息对象缓存值服务器
     *
     * @param msg 待发送的消息对象
     */
    public void saveMsg(BaseMsg msg) {
        obtainDeviceItem(msg.getGroupId(), msg.getDeviceId()).saveMsg(msg);
    }

    /**
     * 已收到已发送消息对象的回复，执行缓存的后续清理
     *
     * @param msg 回复消息对象
     */
    public void removeMsg(MsgReplyNormal msg) {
        obtainDeviceItem(msg.getGroupId(), msg.getDeviceId()).removeMsg(msg);
    }

    /**
     * 「TCP待发送反馈」获取 TCP 通道待发送的消息统计概览
     *
     * @param isFlat   扁平化消息细节
     * @param msgLimit 消息对象的最大数量
     * @return 整体待发送消息统计
     */
    public BaseMsgSending getMsgSendingOutline(boolean isFlat, int msgLimit) {

        List<DeviceGroupedMsgSending> msgSendingGroupedItems = Arrays.stream(DEVICE_ITEMS).map(
                groupItem -> DeviceGroupedMsgSending.createOutline(groupItem[0].getGroupId(),
                        Arrays.stream(groupItem).mapToInt(DeviceItem::getMsgCount).sum(),
                        Arrays.stream(groupItem).mapToInt(DeviceItem::getMsgSendingCount).sum()))
                .filter(item -> item.getMsgCount() != 0)
                .collect(Collectors.toList());

        int msgCount = msgSendingGroupedItems.stream().mapToInt(DeviceGroupedMsgSending::getMsgCount).sum();
        int msgSendingCount = msgSendingGroupedItems.stream().mapToInt(DeviceGroupedMsgSending::getMsgSendingCount).sum();
        if (isFlat) {
            return MachineMsgSending.createFlat(msgCount, msgSendingCount, Arrays.stream(DEVICE_ITEMS)
                    .flatMap(Arrays::stream)
                    .map(DeviceItem::getCacheMsg)
                    .flatMap(Collection::stream)
                    .limit(msgLimit)
                    .map(MsgSending::new)
                    .collect(Collectors.toList()));
        } else {
            return MachineMsgSending.createGather(msgCount, msgSendingCount, msgSendingGroupedItems);
        }
    }

    /**
     * 「TCP待发送反馈」获取 TCP 通道待发送的消息统计概览
     *
     * @param groupId  组号
     * @param isFlat   扁平化消息细节
     * @param msgLimit 消息对象的最大数量
     * @return 设备组待发送消息统计
     */
    public BaseMsgSending getMsgSendingGrouped(int groupId, boolean isFlat, int msgLimit) {
        DeviceItem[] deviceItems = obtainDeviceGroup(groupId);
        int msgCount = Arrays.stream(deviceItems).mapToInt(DeviceItem::getMsgCount).sum();
        int msgSendingCount = Arrays.stream(deviceItems).mapToInt(DeviceItem::getMsgSendingCount).sum();
        if (isFlat) {
            return DeviceGroupedMsgSending.createFlat(groupId, msgCount, msgSendingCount, Arrays.stream(obtainDeviceGroup(groupId))
                    .map(DeviceItem::getCacheMsg)
                    .flatMap(Collection::stream)
                    .limit(msgLimit)
                    .map(MsgSending::new)
                    .collect(Collectors.toList()));
        } else {
            return DeviceGroupedMsgSending.createGather(groupId, msgCount, msgSendingCount, Arrays.stream(obtainDeviceGroup(groupId))
                    .filter(item -> item.getMsgCount() != 0)
                    .map(item -> DeviceItemMsgSending.create(item, false, msgLimit))
                    .collect(Collectors.toList()));
        }
    }

    /**
     * 「TCP待发送反馈」获取 TCP 通道待发送的消息统计概览
     *
     * @param groupId  组号
     * @param deviceId 设备号
     * @param msgLimit 消息对象的最大数量
     * @return 设备待发送消息统计
     */
    public BaseMsgSending getMsgSendingByCoordinate(int groupId, int deviceId, int msgLimit) {
        return DeviceItemMsgSending.create(obtainDeviceItem(groupId, deviceId), true, msgLimit);
    }
}
