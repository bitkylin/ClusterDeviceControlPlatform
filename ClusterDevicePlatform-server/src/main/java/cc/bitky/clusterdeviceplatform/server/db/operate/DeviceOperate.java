package cc.bitky.clusterdeviceplatform.server.db.operate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msg.MsgReplyChargeStatus;
import cc.bitky.clusterdeviceplatform.server.config.WebSetting;
import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.db.repository.DeviceRepository;

@Repository
public class DeviceOperate {

    private final DeviceRepository repository;
    private Logger logger = LoggerFactory.getLogger(getClass());

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
     * 处理设备状态包，更新设备的状态信息
     *
     * @param msgStatus 设备状态包
     * @return null: 无法找到指定的设备；device.status 为 -1: 指定设备未进行任何状态更新
     */
    public Device handleMsgDeviceStatus(MsgReplyChargeStatus msgStatus) {
        Device device = repository.findFirstByGroupIdAndDeviceId(msgStatus.getGroupId(), msgStatus.getDeviceId());
        if (device == null) {
            return null;
        }
        int rawStatus = device.getStatus();
        int newStatus = msgStatus.getStatus();

        if (newStatus == rawStatus) {
            logger.info("设备「" + msgStatus.getGroupId() + ", " + msgStatus.getDeviceId() + "」『"
                    + rawStatus + "->" + newStatus + "』: 状态无更新");
            device.setStatus(ChargeStatus.FRAME_EXCEPTION);
            return device;
        }

        if (rawStatus == ChargeStatus.CHARGING && newStatus == ChargeStatus.FULL && device.getRemainChargeTime() > 0) {
            device.setRemainChargeTime(device.getRemainChargeTime() - 1);
        }
        device.setStatus(newStatus);
        device.setStatusTime(new Date());
        repository.save(device);
        logger.info("设备「" + msgStatus.getGroupId() + ", " + msgStatus.getDeviceId() + "」『"
                + rawStatus + "->" + newStatus + "』: 状态成功更新！");
        return device;
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
