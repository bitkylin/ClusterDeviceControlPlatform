package cc.bitky.clusterdeviceplatform.server.db.operate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import cc.bitky.clusterdeviceplatform.server.config.WebSetting;
import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.db.repository.DeviceRepository;

@Repository
public class DeviceOperate {

    private final DeviceRepository repository;

    @Autowired
    public DeviceOperate(DeviceRepository repository) {
        this.repository = repository;
    }

    /**
     * 获取设备的集合
     *
     * @param groupId  设备组 Id
     * @param deviceId 设备 Id
     * @return 设备的集合
     */
    public List<Device> queryDeviceInfo(int groupId, int deviceId) {
        List<Device> devices;
        if (deviceId == WebSetting.BROADCAST_DEVICE_ID) {
            devices = repository.findByGroupId(groupId);
        } else {
            devices = repository.findByGroupIdAndDeviceId(groupId, deviceId);
        }
        if (devices == null) {
            devices = new ArrayList<>(1);
        }
        return devices;
    }

    /**
     * 保存特定的设备
     *
     * @param device 特定的设备
     * @return 特定的设备
     */
    public Device saveDeviceInfo(Device device) {
        return repository.save(device);
    }
}
