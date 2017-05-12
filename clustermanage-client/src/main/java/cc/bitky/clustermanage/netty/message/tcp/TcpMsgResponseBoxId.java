package cc.bitky.clustermanage.netty.message.tcp;


import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseTcpResponseMsg;

/**
 * 设备回复: 设备 Id 更新
 */
public class TcpMsgResponseBoxId extends BaseTcpResponseMsg {

    public TcpMsgResponseBoxId(int groupId, int boxId, int status) {
        super(groupId, boxId, status);
        setMsgId(MsgType.DEVICE_RESPONSE_DEVICE_ID);
    }
}
