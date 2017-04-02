package cc.bitky.clustermanage.tcp.server.dynamickeeper;

import cc.bitky.clustermanage.db.DeviceGroupBuilder;
import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.bean.DeviceGroup;
import cc.bitky.clustermanage.server.message.ChargeStatus;
import cc.bitky.clustermanage.server.message.HeartBeat;
import cc.bitky.clustermanage.server.message.IMessage;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Maintainer {
  static Logger logger = LoggerFactory.getLogger(Maintainer.class);
  private static List<DeviceGroup> mineLampShelves = new ArrayList<>();

  //public static void process(IMessage message) {
  //  adjustMineLampShelfSize(message);
  //  int msgId = message.getMsgId();
  //  switch (msgId) {
  //    case MsgType.HEART_BEAT:
  //      heartBeatHandle((HeartBeat) message);
  //      break;
  //    case MsgType.CHANGE_STATUS:
  //      chargeStatusHandle((ChargeStatus) message);
  //      break;
  //  }
  //}

  /**
   * 调整充电架数量，使之满足获取到的数据帧的要求
   *
   * @param message 获取到的数据帧
   */
  private static void adjustMineLampShelfSize(IMessage message) {
    int groupId = message.getGroupId();

    while (mineLampShelves.size() < groupId) {
      int originId = mineLampShelves.size() + 1;
      DeviceGroup deviceGroup = DeviceGroupBuilder.builder().setId(originId).build();
      mineLampShelves.add(deviceGroup);
    }
  }

  private static void heartBeatHandle(HeartBeat heartBeat) {
    mineLampShelves.get(heartBeat.getGroupId() - 1).setHeartBeat(System.currentTimeMillis());
    logger.debug("充电架(" + heartBeat.getGroupId() + ") 心跳包处理完毕");
  }

  private static void chargeStatusHandle(ChargeStatus chargeStatus) {
    Device device = mineLampShelves.get(chargeStatus.getGroupId() - 1)
        .getDevices()
        .get(chargeStatus.getBoxId() - 1);

    device.setStatus(chargeStatus.getStatus());
    device.setTime(chargeStatus.getTime());
    logger.debug("充电架(" + chargeStatus.getGroupId() + ") " + chargeStatus.getBoxId() + "号柜, 状态更新");
  }
}
