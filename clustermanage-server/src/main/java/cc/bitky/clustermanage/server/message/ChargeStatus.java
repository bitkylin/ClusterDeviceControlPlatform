package cc.bitky.clustermanage.server.message;

import cc.bitky.clustermanage.tcp.util.enumky.MsgType;

public class ChargeStatus extends BaseMessage {
  private int status;
  private long time;

  public ChargeStatus(int groupId, int boxId, int status) {
    super(groupId, boxId);
    this.status = status;
    this.time = System.currentTimeMillis();
    setMsgId(MsgType.CHANGE_STATUS);
  }

  public int getStatus() {
    return status;
  }

  public long getTime() {
    return time;
  }
}
