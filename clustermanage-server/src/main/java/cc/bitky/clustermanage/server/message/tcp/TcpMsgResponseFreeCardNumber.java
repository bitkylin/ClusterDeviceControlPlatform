package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseTcpResponseMsg;

/**
 * 设备回复: 员工部门更新
 */
public class TcpMsgResponseFreeCardNumber extends BaseTcpResponseMsg {

    public TcpMsgResponseFreeCardNumber(int groupId, int boxId, int status) {
        super(groupId, boxId, status);
        setMsgId(MsgType.DEVICE_RESPONSE_FREE_CARD_NUMBER);
    }

    public TcpMsgResponseFreeCardNumber(int groupId, int boxId, int msgId, int status) {
        super(groupId, boxId, status);
        setMsgId(msgId);
    }
}
