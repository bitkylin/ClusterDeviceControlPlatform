package cc.bitky.clustermanage.web;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.web.bean.WebEmployee;

/**
 * 用于 Spring MVC 的 RESTful API 处理服务
 * 接收并处理 Web 消息
 */
@Service
public class WebMessageHandler {

    /**
     * 更新设备的信息
     *
     * @param webEmployee 员工bean
     * @return 是否更新成功
     */
    List<IMessage> handleEmployeeUpdate(WebEmployee webEmployee) {
        List<IMessage> messages = new ArrayList<>();

        if (webEmployee.getName() != null) {
            messages.add(new WebMsgDeployEmployeeName(webEmployee.getGroupId(), webEmployee.getDeviceId(), webEmployee.getName()));
        }
        if (webEmployee.getDepartment() != null) {
            messages.add(new WebMsgDeployEmployeeDepartment(webEmployee.getGroupId(), webEmployee.getDeviceId(), webEmployee.getDepartment()));
        }
        if (webEmployee.getCardNumber() != 0) {
            messages.add(new WebMsgDeployEmployeeCardNumber(webEmployee.getGroupId(), webEmployee.getDeviceId(), webEmployee.getCardNumber()));
        }
        if (webEmployee.getNewId() != 0) {
            messages.add(new WebMsgDeployEmployeeDeviceId(webEmployee.getGroupId(), webEmployee.getDeviceId(), webEmployee.getNewId()));
        }
        return messages;
    }
}
