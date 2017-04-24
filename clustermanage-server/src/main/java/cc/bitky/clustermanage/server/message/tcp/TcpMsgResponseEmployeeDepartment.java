package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMessage;

/**
 * 设备回复: 员工部门更新
 */
public class TcpMsgResponseEmployeeDepartment extends BaseMessage {

    public TcpMsgResponseEmployeeDepartment(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_1);
    }
}
