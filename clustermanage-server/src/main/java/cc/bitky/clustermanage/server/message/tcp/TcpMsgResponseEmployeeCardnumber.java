package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseTcpResponseMsg;

/**
 * 设备回复: 员工卡号更新
 */
public class TcpMsgResponseEmployeeCardnumber extends BaseTcpResponseMsg {

    public TcpMsgResponseEmployeeCardnumber(int groupId, int boxId, int status) {
        super(groupId, boxId, status);
        setMsgId(MsgType.DEVICE_RESPONSE_EMPLOYEE_CARD_NUMBER);
    }
}
