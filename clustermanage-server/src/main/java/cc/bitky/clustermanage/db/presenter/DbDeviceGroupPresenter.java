package cc.bitky.clustermanage.db.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.bean.DeviceGroup;
import cc.bitky.clustermanage.db.repository.DeviceGroupRepository;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgDeviceStatus;

@Repository
public class DbDeviceGroupPresenter {
    private final DeviceGroupRepository deviceGroupRepository;
    private final DbDevicePresenter dbDevicePresenter;

    private Logger logger = LoggerFactory.getLogger(DbDeviceGroupPresenter.class);

    @Autowired
    public DbDeviceGroupPresenter(DeviceGroupRepository deviceGroupRepository, DbDevicePresenter dbDevicePresenter) {
        this.deviceGroupRepository = deviceGroupRepository;
        this.dbDevicePresenter = dbDevicePresenter;

    }

    /**
     * 调整(或增加)设备的数量，使之满足获取到的数据帧的要求
     *
     * @param groupId 获取到的数据帧的组 Id
     */
    private void initDbDeviceGroup(int groupId) {
        int groupSize = (int) deviceGroupRepository.count();

        while (groupSize < groupId) {
            groupSize++;
            deviceGroupRepository.save(new DeviceGroup(groupSize));
            dbDevicePresenter.InitDbDevices(groupSize);
        }
    }

    /**
     * 处理心跳包
     *
     * @param tcpMsgHeartBeat 心跳包
     */
    public void handleMsgHeartBeat(IMessage tcpMsgHeartBeat) {
        initDbDeviceGroup(tcpMsgHeartBeat.getGroupId());

        DeviceGroup deviceGroup = deviceGroupRepository.findByGroupId(tcpMsgHeartBeat.getGroupId());
        deviceGroup.setHeartBeatTime(System.currentTimeMillis());
        deviceGroupRepository.save(deviceGroup);
        logger.info("设备组(" + tcpMsgHeartBeat.getGroupId() + ") 心跳包处理完毕");
    }

    /**
     * 处理设备状态包
     *
     * @param tcpMsgDeviceStatus 设备状态包
     */
    public void handleMsgDeviceStatus(TcpMsgDeviceStatus tcpMsgDeviceStatus) {
        handleMsgHeartBeat(tcpMsgDeviceStatus);
        dbDevicePresenter.handleMsgDeviceStatus(tcpMsgDeviceStatus);
    }

    /**
     * 获得所需的充电柜
     */
    public List<Device> getDevices(int groupId, int boxId) {
        return dbDevicePresenter.getDevices(groupId, boxId);
    }
}
