package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.BaseMessage;
import cc.bitky.clustermanage.server.MsgType;

/**
 * 设备回复: 充电状态
 */
public class TcpMsgDeviceStatus extends BaseMessage {
  private int status;
  private long time;

  public TcpMsgDeviceStatus(int groupId, int boxId, int status) {
    super(groupId, boxId);
    this.status = status;
    this.time = System.currentTimeMillis();
    setMsgId(MsgType.DEVICE_RESPONSE_STATUS);
  }

  public int getStatus() {
    return status;
  }

  public long getTime() {
    return time;
  }
}
