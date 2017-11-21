package cc.bitky.clusterdeviceplatform.server.db.operate;

import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceArray;

import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.config.DeviceSetting;

@Repository
public class DeviceStatusRepo {
    /**
     * 设备状态暂存
     */
    private final AtomicReferenceArray<AtomicIntegerArray> STATUS_STORAGE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);

    {
        for (int i = 1; i < STATUS_STORAGE.length(); i++) {
            AtomicIntegerArray array = new AtomicIntegerArray(DeviceSetting.MAX_DEVICE_ID + 1);
            for (int j = 0; j <= DeviceSetting.MAX_DEVICE_ID; j++) {
                array.set(j, ChargeStatus.FRAME_EXCEPTION);
            }
            STATUS_STORAGE.set(i, array);
        }
    }

    /**
     * 从服务器暂存库中，获取设备状态
     *
     * @param groupId  设备组号
     * @param deviceId 设备号
     * @return 指定设备的状态
     */
    public int getStatus(int groupId, int deviceId) {
        AtomicIntegerArray array = STATUS_STORAGE.get(groupId);
        return array.get(deviceId);
    }

    public void setStatus(int groupId, int deviceId, int status) {
        AtomicIntegerArray array = STATUS_STORAGE.get(groupId);
        array.set(deviceId, status);
    }
}
