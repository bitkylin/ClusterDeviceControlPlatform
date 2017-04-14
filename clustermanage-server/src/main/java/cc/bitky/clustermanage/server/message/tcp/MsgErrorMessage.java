package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.BaseMessage;
import cc.bitky.clustermanage.server.MsgType;

public class MsgErrorMessage extends BaseMessage {

  private String msg;

  public MsgErrorMessage(int groupId) {
    super(groupId);
    setMsgId(MsgType.ERROR);
  }

  public MsgErrorMessage() {
    this(-1);
  }

  public MsgErrorMessage(int groupId, String msg) {
    this(groupId);
    this.msg = msg;
  }

  public MsgErrorMessage(String msg) {
    this(-1, msg);
  }
}
