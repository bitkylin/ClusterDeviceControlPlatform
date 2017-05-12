package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseTcpResponseMsg;

/**
 * 设备回复: 员工部门更新「1」
 */
public class TcpMsgResponseEmployeeDepartment1 extends BaseTcpResponseMsg {

    public TcpMsgResponseEmployeeDepartment1(int groupId, int boxId, int status) {
        super(groupId, boxId, status);
        setMsgId(MsgType.DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_1);
    }
}
