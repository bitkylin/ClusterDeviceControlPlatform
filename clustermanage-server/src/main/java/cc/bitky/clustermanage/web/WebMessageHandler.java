package cc.bitky.clustermanage.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import cc.bitky.clustermanage.db.bean.Employee;
import cc.bitky.clustermanage.server.bean.ServerWebMessageHandler;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.server.message.web.WebMsgOperateBoxUnlock;

/**
 * 用于 Spring MVC 的 RESTful API 处理服务
 * 接收并处理 Web 消息
 */
@Service
public class WebMessageHandler {

    private final ServerWebMessageHandler serverWebMessageHandler;

    @Autowired
    public WebMessageHandler(ServerWebMessageHandler serverWebMessageHandler) {
        this.serverWebMessageHandler = serverWebMessageHandler;
    }

    /**
     * 更新设备的信息
     *
     * @param employee 员工bean
     * @return 是否更新成功
     */
    boolean handleEmployeeUpdate(Employee employee) {
        List<IMessage> messages = new ArrayList<>();

        if (employee.getDeviceId() == 255) {
            for (int i = 0; i < 100; i++) {
                if (employee.getName() != null) {
                    messages.add(new WebMsgDeployEmployeeName(employee.getGroupId(), i, employee.getName()));
                }
                if (employee.getDepartment() != null) {
                    messages.add(new WebMsgDeployEmployeeDepartment(employee.getGroupId(), i, employee.getDepartment()));
                }
                if (employee.getCardNumber() != 0) {
                    messages.add(new WebMsgDeployEmployeeCardNumber(employee.getGroupId(), i, employee.getCardNumber()));
                }
            }
            if (messages.size() != 0) {
                return serverWebMessageHandler.handleWebMsg(messages);
            }
        }

        if (employee.getName() != null) {
            messages.add(new WebMsgDeployEmployeeName(employee.getGroupId(), employee.getDeviceId(), employee.getName()));
        }
        if (employee.getDepartment() != null) {
            messages.add(new WebMsgDeployEmployeeDepartment(employee.getGroupId(), employee.getDeviceId(), employee.getDepartment()));
        }
        if (employee.getCardNumber() != 0) {
            messages.add(new WebMsgDeployEmployeeCardNumber(employee.getGroupId(), employee.getDeviceId(), employee.getCardNumber()));
        }
        if (messages.size() != 0) {
            return serverWebMessageHandler.handleWebMsg(messages);
        }
        return false;
    }

    /**
     * 设备开锁
     *
     * @param webMsgOperateBoxUnlock 设备开锁bean
     * @return 是否开锁成功
     */
    boolean handleOperateUnlock(WebMsgOperateBoxUnlock webMsgOperateBoxUnlock) {
        List<IMessage> messages = new ArrayList<>();

        if (webMsgOperateBoxUnlock.getBoxId() == 255) {
            for (int i = 0; i < 100; i++) {
                messages.add(new WebMsgOperateBoxUnlock(webMsgOperateBoxUnlock.getGroupId(), i));
            }
            return serverWebMessageHandler.handleWebMsg(messages);
        }

        messages.add(webMsgOperateBoxUnlock);
        return serverWebMessageHandler.handleWebMsg(messages);
    }
}
