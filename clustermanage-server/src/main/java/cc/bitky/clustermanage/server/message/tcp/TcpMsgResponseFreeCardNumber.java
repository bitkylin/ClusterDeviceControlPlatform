package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMessage;

/**
 * 设备回复: 员工部门更新
 */
public class TcpMsgResponseFreeCardNumber extends BaseMessage {

    public TcpMsgResponseFreeCardNumber(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.DEVICE_RESPONSE_FREE_CARD_NUMBER);
    }
}
