package cc.bitky.clustermanage.netty.message.web;


import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.WebMsgBaseEmployee;

/**
 * 服务器部署员工部门
 */
public class WebMsgDeployEmployeeDepartment2 extends WebMsgBaseEmployee {
    public WebMsgDeployEmployeeDepartment2(int groupId, int boxId, String value) {
        super(groupId, boxId, value);
        setMsgId(MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_2);
    }
}
