package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseTcpResponseMsg;

/**
 * 设备回复: 员工姓名更新
 */
public class TcpMsgResponseEmployeeName extends BaseTcpResponseMsg {

    public TcpMsgResponseEmployeeName(int groupId, int boxId, int status) {
        super(groupId, boxId, status);
        setMsgId(MsgType.DEVICE_RESPONSE_EMPLOYEE_NAME);
    }
}
