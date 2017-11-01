package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseTcpResponseMsg;

/**
 * 设备回复: 员工部门更新
 */
public class TcpMsgResponseFreeCardNumber extends BaseTcpResponseMsg {

    private TcpMsgResponseFreeCardNumber(int groupId, int deviceId, int status) {
        super(groupId, deviceId, status);
        setMsgId(MsgType.DEVICE_RESPONSE_FREE_CARD_NUMBER);
    }

    public TcpMsgResponseFreeCardNumber(int groupId, int deviceId, int msgId, int status) {
        super(groupId, deviceId, status);
        setMsgId(msgId);
    }
}
