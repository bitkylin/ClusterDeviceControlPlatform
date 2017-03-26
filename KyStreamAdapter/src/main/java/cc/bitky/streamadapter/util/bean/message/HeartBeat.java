package cc.bitky.streamadapter.util.bean.message;

import cc.bitky.streamadapter.util.enumky.MsgType;

public class HeartBeat extends BaseMessage {

  public HeartBeat(int groupId) {
    super(groupId);
    setMsgId(MsgType.HEART_BEAT);
  }
}
