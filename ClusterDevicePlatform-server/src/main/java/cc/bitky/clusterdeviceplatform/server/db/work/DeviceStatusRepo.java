package cc.bitky.clusterdeviceplatform.server.db.work;

import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;
import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.db.bean.routineinfo.StatusItem;

@Repository
public class DeviceStatusRepo {
    /**
     * 设备状态暂存
     */
    private final AtomicReferenceArray<AtomicReferenceArray<StatusItem>> CHARGE_STATUS_STORAGE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
    private final AtomicReferenceArray<AtomicReferenceArray<StatusItem>> WORK_STATUS_STORAGE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
    /**
     * 设备组最近通信时间
     */
    private final AtomicLongArray DEVICEGROUP_RECENT_COMM = new AtomicLongArray(DeviceSetting.MAX_GROUP_ID + 1);

    {
        for (int i = 1; i < CHARGE_STATUS_STORAGE.length(); i++) {
            AtomicReferenceArray<StatusItem> array1 = new AtomicReferenceArray<>(DeviceSetting.MAX_DEVICE_ID + 1);
            AtomicReferenceArray<StatusItem> array2 = new AtomicReferenceArray<>(DeviceSetting.MAX_DEVICE_ID + 1);
            for (int j = 0; j <= DeviceSetting.MAX_DEVICE_ID; j++) {
                array1.set(j, new StatusItem());
                array2.set(j, new StatusItem());
            }
            CHARGE_STATUS_STORAGE.set(i, array1);
            WORK_STATUS_STORAGE.set(i, array2);
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
        AtomicReferenceArray<StatusItem> array = CHARGE_STATUS_STORAGE.get(groupId);
        return array.get(deviceId);
    }

    private void setChargeStatus(int groupId, int deviceId, StatusItem item) {
        AtomicReferenceArray<StatusItem> array = CHARGE_STATUS_STORAGE.get(groupId);
        array.set(deviceId, item);
    }

    private StatusItem getWorkStatus(int groupId, int deviceId) {
        AtomicReferenceArray<StatusItem> array = WORK_STATUS_STORAGE.get(groupId);
        return array.get(deviceId);
    }

    private void setWorkStatus(int groupId, int deviceId, StatusItem item) {
        AtomicReferenceArray<StatusItem> array = WORK_STATUS_STORAGE.get(groupId);
        array.set(deviceId, item);
    }

    /**
     * 获取设备组最近通信时刻的时间戳
     *
     * @param groupId 设备组 ID
     * @return 特定的时间戳
     */
    public long getDeviceGroupRecentCommTime(int groupId) {
        if (groupId > DeviceSetting.MAX_GROUP_ID || groupId <= 0) {
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
        if (groupId > DeviceSetting.MAX_GROUP_ID || groupId <= 0) {
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
        if (groupId > DeviceSetting.MAX_GROUP_ID || groupId <= 0 || deviceId > DeviceSetting.MAX_DEVICE_ID || deviceId <= 0) {
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
}
