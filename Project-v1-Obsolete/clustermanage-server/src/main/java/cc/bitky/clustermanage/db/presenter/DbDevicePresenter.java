package cc.bitky.clustermanage.db.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.repository.DeviceRepository;
import cc.bitky.clustermanage.global.ServerSetting;
import cc.bitky.clustermanage.server.message.ChargeStatus;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseStatus;

@Service
class DbDevicePresenter {
    private final DeviceRepository deviceRepository;
    private Logger logger = LoggerFactory.getLogger(DbDevicePresenter.class);

    @Autowired
    public DbDevicePresenter(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * 处理设备状态包，更新设备的状态信息
     *
     * @param msgStatus 设备状态包
     * @return null: 无法找到指定的设备；device.status 为 -1: 指定设备未进行任何状态更新
     */
    Device handleMsgDeviceStatus(TcpMsgResponseStatus msgStatus) {
        Device device = deviceRepository.findFirstByGroupIdAndDeviceId(msgStatus.getGroupId(), msgStatus.getDeviceId());
        if (device == null) return null;
        int rawStatus = device.getStatus();
        int newStatus = msgStatus.getStatus();
        if (newStatus > 6 || newStatus < 0) newStatus = ChargeStatus.CRASH;

        if (newStatus == rawStatus) {
            logger.info("设备「" + msgStatus.getGroupId() + ", " + msgStatus.getDeviceId() + "」『"
                    + rawStatus + "->" + newStatus + "』: 状态无更新");
            device.setStatus(-1);
            return device;
        }

        if (rawStatus == ChargeStatus.CHARGING && newStatus == ChargeStatus.FULL && device.getRemainChargeTime() > 0) {
            device.setRemainChargeTime(device.getRemainChargeTime() - 1);
        }
        device.setStatus(newStatus);
        device.setStatusTime(new Date(msgStatus.getTime()));
        deviceRepository.save(device);
        logger.info("设备「" + msgStatus.getGroupId() + ", " + msgStatus.getDeviceId() + "」『"
                + rawStatus + "->" + newStatus + "』: 状态成功更新！");
        return device;
    }

    /**
     * 获取设备的集合
     *
     * @param groupId  组 Id
     * @param deviceId 设备 Id
     * @return 设备的集合
     */
    List<Device> getDevices(int groupId, int deviceId) {
        if (deviceId == 255) {
            List<Device> devices = deviceRepository.findByGroupId(groupId);
            if (devices == null) devices = new ArrayList<>(1);
            return devices;
        }
        List<Device> devices = new ArrayList<>(1);
        if (deviceId >= 1 && deviceId <= ServerSetting.MAX_DEVICE_SIZE_EACH_GROUP) {
            devices.add(deviceRepository.findFirstByGroupIdAndDeviceId(groupId, deviceId));
        }
        return devices;
    }

    /**
     * 更新设备
     *
     * @param device 设备 bean
     */
    void updateDevice(Device device) {
        deviceRepository.save(device);
    }
}
