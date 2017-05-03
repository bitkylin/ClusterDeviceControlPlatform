package cc.bitky.clustermanage.netty.message.tcp;


import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

public class TcpMsgHeartBeat extends BaseMessage {

  public TcpMsgHeartBeat(int groupId) {
    super(groupId);
    setMsgId(MsgType.HEART_BEAT);
  }
}
