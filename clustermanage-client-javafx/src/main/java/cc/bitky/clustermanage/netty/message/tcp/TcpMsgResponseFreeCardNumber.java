package cc.bitky.clustermanage.netty.message.tcp;


import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseTcpResponseMsg;

/**
 * 设备回复: 员工部门更新
 */
public class TcpMsgResponseFreeCardNumber extends BaseTcpResponseMsg {

    public TcpMsgResponseFreeCardNumber(int groupId, int boxId, int status, int item) {
        super(groupId, boxId, status);
        setMsgId(MsgType.DEVICE_RESPONSE_FREE_CARD_NUMBER + item);
    }
}
