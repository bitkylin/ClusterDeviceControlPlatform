package cc.bitky.streamadapter.util.bean.message;

import cc.bitky.streamadapter.util.enumky.ChargeStatusEnum;
import cc.bitky.streamadapter.util.enumky.MsgType;

public class ChargeStatus extends BaseMessage {
  private ChargeStatusEnum status;
  private long time;

  public ChargeStatus(int groupId, int boxId, ChargeStatusEnum status) {
    super(groupId, boxId);
    this.status = status;
    this.time = System.currentTimeMillis();
    setMsgId(MsgType.CHANGE_STATUS);
  }

  public ChargeStatusEnum getStatus() {
    return status;
  }

  public long getTime() {
    return time;
  }
}
