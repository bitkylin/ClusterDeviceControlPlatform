package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.base.BaseMessage;
import cc.bitky.clustermanage.server.message.MsgType;

public class TcpMsgHeartBeat extends BaseMessage {

  public TcpMsgHeartBeat(int groupId) {
    super(groupId);
    setMsgId(MsgType.HEART_BEAT);
  }
}
