package cc.bitky.clustermanage.tcp.util.bean.message;

import cc.bitky.clustermanage.tcp.util.enumky.MsgType;

public class HeartBeat extends BaseMessage {

  public HeartBeat(int groupId) {
    super(groupId);
    setMsgId(MsgType.HEART_BEAT);
  }
}
