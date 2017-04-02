package cc.bitky.clustermanage.db.presenter;

import cc.bitky.clustermanage.db.DeviceGroupBuilder;
import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.bean.DeviceGroup;
import cc.bitky.clustermanage.db.repository.DeviceGroupRepository;
import cc.bitky.clustermanage.server.bean.MessageHandler;
import cc.bitky.clustermanage.server.message.ChargeStatus;
import cc.bitky.clustermanage.server.message.HeartBeat;
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

  public void heartBeatHandle(HeartBeat heartBeat) {
    DeviceGroup deviceGroup = deviceGroupRepository.findByName(heartBeat.getGroupId());
    deviceGroup.setHeartBeat(System.currentTimeMillis());
    deviceGroupRepository.save(deviceGroup);
    logger.info("充电架(" + heartBeat.getGroupId() + ") 心跳包处理完毕");
  }

  public void chargeStatusHandle(ChargeStatus chargeStatus) {
    DeviceGroup deviceGroup = deviceGroupRepository.findByName(chargeStatus.getGroupId());
    Device device = deviceGroup.getDevices().get(chargeStatus.getBoxId() - 1);
    device.setStatus(chargeStatus.getStatus());
    device.setTime(chargeStatus.getTime());
    deviceGroupRepository.save(deviceGroup);

    logger.info("充电架(" + chargeStatus.getGroupId() + ") " + chargeStatus.getBoxId() + "号柜, 状态更新");
  }

  private boolean isLegal(IMessage message) {
    logger.warn("接收数据异常，接收数据标号超出数据库已存的范围：" + message.getGroupId());
    return true;
  }
}
