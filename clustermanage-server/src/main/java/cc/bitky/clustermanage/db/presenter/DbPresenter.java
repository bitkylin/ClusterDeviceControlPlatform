package cc.bitky.clustermanage.db.presenter;

import cc.bitky.clustermanage.db.DeviceGroupBuilder;
import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.bean.DeviceGroup;
import cc.bitky.clustermanage.db.repository.DeviceGroupRepository;
import cc.bitky.clustermanage.server.bean.MessageHandler;
import cc.bitky.clustermanage.server.message.MsgChargeStatus;
import cc.bitky.clustermanage.server.message.MsgHeartBeat;
import cc.bitky.clustermanage.server.message.IMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DbPresenter {
  private final DeviceGroupRepository deviceGroupRepository;
  private Logger logger = LoggerFactory.getLogger(MessageHandler.class);
  private int groupSize = -1;

  @Autowired
  public DbPresenter(DeviceGroupRepository deviceGroupRepository) {
    this.deviceGroupRepository = deviceGroupRepository;
    groupSize = (int) deviceGroupRepository.count();
  }

  /**
   * 调整充电架数量，使之满足获取到的数据帧的要求
   *
   * @param groupId 获取到的数据帧的组Id
   */
  public void initDb(int groupId) {
    while (groupSize < groupId) {
      groupSize++;
      DeviceGroup deviceGroup = DeviceGroupBuilder.builder().setId(groupSize).build();
      deviceGroupRepository.save(deviceGroup);
    }
  }

  public void heartBeatHandle(MsgHeartBeat msgHeartBeat) {
    DeviceGroup deviceGroup = deviceGroupRepository.findByName(msgHeartBeat.getGroupId());
    deviceGroup.setHeartBeat(System.currentTimeMillis());
    deviceGroupRepository.save(deviceGroup);
    logger.info("充电架(" + msgHeartBeat.getGroupId() + ") 心跳包处理完毕");
  }

  public void chargeStatusHandle(MsgChargeStatus msgChargeStatus) {
    DeviceGroup deviceGroup = deviceGroupRepository.findByName(msgChargeStatus.getGroupId());
    Device device = deviceGroup.getDevices().get(msgChargeStatus.getBoxId() - 1);
    device.setStatus(msgChargeStatus.getStatus());
    device.setTime(msgChargeStatus.getTime());
    deviceGroupRepository.save(deviceGroup);

    logger.info("充电架(" + msgChargeStatus.getGroupId() + ") " + msgChargeStatus.getBoxId() + "号柜, 状态更新");
  }

  private boolean isLegal(IMessage message) {
    logger.warn("接收数据异常，接收数据标号超出数据库已存的范围：" + message.getGroupId());
    return true;
  }
}
