package cc.bitky.clustermanage.server.message;

import cc.bitky.clustermanage.tcp.util.enumky.MsgType;

public class MsgHeartBeat extends BaseMessage {

  public MsgHeartBeat(int groupId) {
    super(groupId);
    setMsgId(MsgType.HEART_BEAT);
  }
}
