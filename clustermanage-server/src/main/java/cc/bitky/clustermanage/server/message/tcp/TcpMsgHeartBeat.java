package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.BaseMessage;
import cc.bitky.clustermanage.server.MsgType;

public class TcpMsgHeartBeat extends BaseMessage {

  public TcpMsgHeartBeat(int groupId) {
    super(groupId);
    setMsgId(MsgType.HEART_BEAT);
  }
}
