package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.web.base.WebMsgBaseEmployee;

/**
 * 服务器部署员工部门
 */
public class WebMsgDeployEmployeeDepartment extends WebMsgBaseEmployee {
    public WebMsgDeployEmployeeDepartment(int groupId, int boxId, String value) {
        super(groupId, boxId, value);
        setMsgId(MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_1);
    }
}
