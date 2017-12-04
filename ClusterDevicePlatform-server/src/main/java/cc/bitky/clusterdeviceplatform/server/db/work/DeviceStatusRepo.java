package cc.bitky.clusterdeviceplatform.server.db.work;

import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.messageutils.msg.MsgReplyDeviceStatus;

@Repository
public class DeviceStatusRepo {
    /**
     * 设备状态暂存
     */
    private final AtomicReferenceArray<AtomicIntegerArray> CHARGE_STATUS_STORAGE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
    private final AtomicReferenceArray<AtomicIntegerArray> WORK_STATUS_STORAGE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);

    {
        for (int i = 1; i < CHARGE_STATUS_STORAGE.length(); i++) {
            AtomicIntegerArray array1 = new AtomicIntegerArray(DeviceSetting.MAX_DEVICE_ID + 1);
            AtomicIntegerArray array2 = new AtomicIntegerArray(DeviceSetting.MAX_DEVICE_ID + 1);
            for (int j = 0; j <= DeviceSetting.MAX_DEVICE_ID; j++) {
                array1.set(j, ChargeStatus.FRAME_EXCEPTION);
                array2.set(j, ChargeStatus.FRAME_EXCEPTION);
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
    private int getChargeStatus(int groupId, int deviceId) {
        AtomicIntegerArray array = CHARGE_STATUS_STORAGE.get(groupId);
        return array.get(deviceId);
    }

    private void setChargeStatus(int groupId, int deviceId, int status) {
        AtomicIntegerArray array = CHARGE_STATUS_STORAGE.get(groupId);
        array.set(deviceId, status);
    }

    private int getWorkStatus(int groupId, int deviceId) {
        AtomicIntegerArray array = WORK_STATUS_STORAGE.get(groupId);
        return array.get(deviceId);
    }

    private void setWorkStatus(int groupId, int deviceId, int status) {
        AtomicIntegerArray array = WORK_STATUS_STORAGE.get(groupId);
        array.set(deviceId, status);
    }

    public int getStatus(int groupId, int deviceId, MsgReplyDeviceStatus.Type type) {
        if (type == MsgReplyDeviceStatus.Type.CHARGE) {
            return getChargeStatus(groupId, deviceId);
        } else {
            return getWorkStatus(groupId, deviceId);
        }
    }

    public void setStatus(int groupId, int deviceId, int status, MsgReplyDeviceStatus.Type type) {
        if (type == MsgReplyDeviceStatus.Type.CHARGE) {
            setChargeStatus(groupId, deviceId, status);
        } else {
            setWorkStatus(groupId, deviceId, status);
        }
    }
}
