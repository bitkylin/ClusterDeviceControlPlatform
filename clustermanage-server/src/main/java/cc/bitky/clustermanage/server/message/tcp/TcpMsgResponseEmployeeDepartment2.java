package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseTcpResponseMsg;

/**
 * 设备回复: 员工部门更新「2」
 */
public class TcpMsgResponseEmployeeDepartment2 extends BaseTcpResponseMsg {

    public TcpMsgResponseEmployeeDepartment2(int groupId, int boxId, int status) {
        super(groupId, boxId, status);
        setMsgId(MsgType.DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_2);
    }
}
