package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMessage;

/**
 * 设备回复: 员工卡号更新
 */
public class TcpMsgResponseEmployeeCardnumber extends BaseMessage {

    public TcpMsgResponseEmployeeCardnumber(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.DEVICE_RESPONSE_EMPLOYEE_CARD_NUMBER);
    }
}
