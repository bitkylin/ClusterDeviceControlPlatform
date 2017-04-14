package cc.bitky.clustermanage.db.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.repository.DeviceRepository;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgDeviceStatus;

@Service
public class DbDevicePresenter {
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
     * 处理设备状态包
     *
     * @param tcpMsgDeviceStatus 设备状态包
     */
    void handleMsgDeviceStatus(TcpMsgDeviceStatus tcpMsgDeviceStatus) {
        Device device = deviceRepository.findFirstByGroupIdAndBoxId(tcpMsgDeviceStatus.getGroupId(), tcpMsgDeviceStatus.getBoxId());
        int rawStatus = device.getStatus();
        if (tcpMsgDeviceStatus.getStatus() == rawStatus) {
            logger.info("设备「" + tcpMsgDeviceStatus.getGroupId() + ", " + tcpMsgDeviceStatus.getBoxId() + "」『" + rawStatus + "』: 状态无更新");
            return;
        }
        device.setStatus(tcpMsgDeviceStatus.getStatus());
        device.setTime(tcpMsgDeviceStatus.getTime());
        deviceRepository.save(device);
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
}
