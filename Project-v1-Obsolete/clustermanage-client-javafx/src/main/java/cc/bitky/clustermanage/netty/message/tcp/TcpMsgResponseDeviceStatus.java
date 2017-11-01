package cc.bitky.clustermanage.netty.message.tcp;


import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseTcpResponseMsg;

/**
 * 设备回复: 充电状态
 */
public class TcpMsgResponseDeviceStatus extends BaseTcpResponseMsg {

    public TcpMsgResponseDeviceStatus(int groupId, int boxId, int status) {
        super(groupId, boxId, status);
        setMsgId(MsgType.DEVICE_RESPONSE_STATUS);
    }
}
