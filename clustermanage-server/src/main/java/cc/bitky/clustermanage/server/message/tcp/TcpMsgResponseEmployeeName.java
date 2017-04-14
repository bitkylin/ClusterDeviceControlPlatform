package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMessage;

/**
 * 设备回复: 员工姓名更新
 */
public class TcpMsgResponseEmployeeName extends BaseMessage {

    public TcpMsgResponseEmployeeName(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.DEVICE_RESPONSE_EMPLOYEE_NAME);
    }
}
