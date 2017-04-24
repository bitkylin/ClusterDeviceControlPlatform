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
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseDeviceStatus;

@Service
class DbDevicePresenter {
    private final DeviceRepository deviceRepository;
    private Logger logger = LoggerFactory.getLogger(DbDevicePresenter.class);

    @Autowired
    public DbDevicePresenter(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }


    /**
     * 调整(或增加)设备的数量，使之满足获取到的数据帧的要求
     *
     * @param groupId 获取到的数据帧的组 Id
     */
    void InitDbDevices(int groupId) {
        List<Device> deviceList = new ArrayList<>(100);
        for (int i = 1; i <= 100; i++) {
            deviceList.add(new Device(groupId, i));
        }
        deviceRepository.save(deviceList);
    }

    /**
     * 处理设备状态包，更新设备的状态信息
     *
     * @param tcpMsgResponseDeviceStatus 设备状态包
     */
    Device handleMsgDeviceStatus(TcpMsgResponseDeviceStatus tcpMsgResponseDeviceStatus) {
        Device device = deviceRepository.findFirstByGroupIdAndBoxId(tcpMsgResponseDeviceStatus.getGroupId(), tcpMsgResponseDeviceStatus.getBoxId());
        if (device == null) return null;
        int rawStatus = device.getStatus();
        if (tcpMsgResponseDeviceStatus.getStatus() == rawStatus) {
            logger.info("设备「" + tcpMsgResponseDeviceStatus.getGroupId() + ", " + tcpMsgResponseDeviceStatus.getBoxId() + "」『" + rawStatus + "』: 状态无更新");
        } else {
            device.setStatus(tcpMsgResponseDeviceStatus.getStatus());
            device.setTime(new Date(tcpMsgResponseDeviceStatus.getTime()));
            deviceRepository.save(device);
            logger.info("设备「" + tcpMsgResponseDeviceStatus.getGroupId() + ", " + tcpMsgResponseDeviceStatus.getBoxId() + "」『" + rawStatus + "』: 状态成功更新！");
        }
        return device;
    }

    /**
     * 获取设备的集合
     *
     * @param groupId 组 Id
     * @param boxId   设备 Id
     * @return 设备的集合
     */
    List<Device> getDevices(int groupId, int boxId) {
        if (boxId == 255) return deviceRepository.findByGroupId(groupId);
        List<Device> devices = new ArrayList<>(1);
        if (boxId >= 1 && boxId <= 100) {
            devices.add(deviceRepository.findFirstByGroupIdAndBoxId(groupId, boxId));
        }
        return devices;
    }

    /**
     * 通过卡号查询相应员工的 objectId
     *
     * @param cardNum 员工卡号
     * @return 相应员工的 objectId
     */
    String obtainEmployeeObjectIdByCardNum(long cardNum) {
        Device device = deviceRepository.findFirstByCardNumber(cardNum);
        return device.getEmployeeObjectId();
    }

    /**
     * 更新设备
     *
     * @param device 设备 bean
     */
    public void updateDevice(Device device) {
        deviceRepository.save(device);
    }
}
