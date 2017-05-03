package cc.bitky.clustermanage.netty.message.tcp;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseTcpResponseMsg;

/**
 * 设备回复: 员工部门更新
 */
public class TcpMsgResponseEmployeeDepartment extends BaseTcpResponseMsg {

    public TcpMsgResponseEmployeeDepartment(int groupId, int boxId, int status) {
        super(groupId, boxId, status);
        setMsgId(MsgType.DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_1);
    }
}
