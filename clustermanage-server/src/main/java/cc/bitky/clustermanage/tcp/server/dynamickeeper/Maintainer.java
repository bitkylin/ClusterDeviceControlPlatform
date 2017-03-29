package cc.bitky.clustermanage.tcp.server.dynamickeeper;

import cc.bitky.clustermanage.tcp.util.bean.database.DoorInfo;
import cc.bitky.clustermanage.tcp.util.bean.database.MineLampShelf;
import cc.bitky.clustermanage.tcp.util.bean.database.MineLampShelfBuilder;
import cc.bitky.clustermanage.tcp.util.bean.message.ChargeStatus;
import cc.bitky.clustermanage.tcp.util.bean.message.HeartBeat;
import cc.bitky.clustermanage.tcp.util.bean.message.IMessage;
import cc.bitky.clustermanage.tcp.util.enumky.MsgType;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Maintainer {
  static Logger logger = LoggerFactory.getLogger(Maintainer.class);
  private static List<MineLampShelf> mineLampShelves = new ArrayList<>();

  public static void process(IMessage message) {
    adjustMineLampShelfSize(message);
    int msgId = message.getMsgId();
    switch (msgId) {
      case MsgType.HEART_BEAT:
        heartBeatHandle((HeartBeat) message);
        break;
      case MsgType.CHANGE_STATUS:
        chargeStatusHandle((ChargeStatus) message);
        break;
    }
  }

  /**
   * 调整充电架数量，使之满足获取到的数据帧的要求
   *
   * @param message 获取到的数据帧
   */
  private static void adjustMineLampShelfSize(IMessage message) {
    int groupId = message.getGroupId();

    while (mineLampShelves.size() < groupId) {
      int originId = mineLampShelves.size() + 1;
      MineLampShelf mineLampShelf = MineLampShelfBuilder.builder().setId(originId).build();
      mineLampShelves.add(mineLampShelf);
    }
  }

  private static void heartBeatHandle(HeartBeat heartBeat) {
    mineLampShelves.get(heartBeat.getGroupId() - 1).setHeartBeat(System.currentTimeMillis());
    logger.debug("充电架(" + heartBeat.getGroupId() + ") 心跳包处理完毕");
  }

  private static void chargeStatusHandle(ChargeStatus chargeStatus) {
    DoorInfo doorInfo = mineLampShelves.get(chargeStatus.getGroupId() - 1)
        .getDoorInfos()
        .get(chargeStatus.getBoxId() - 1);

    doorInfo.setMinerLampStatus(chargeStatus.getStatus().ordinal());
    doorInfo.setStatusTime(chargeStatus.getTime());
    logger.debug("充电架(" + chargeStatus.getGroupId() + ") " + chargeStatus.getBoxId() + "号柜, 状态更新");
  }
}
