package cc.bitky.clustermanage.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import cc.bitky.clustermanage.server.bean.ServerWebMessageHandler;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.server.message.web.WebMsgOperateBoxUnlock;
import cc.bitky.clustermanage.web.bean.WebEmployee;

@RestController
@RequestMapping(value = "/operate")
public class OperateRestController {

    private final ServerWebMessageHandler serverWebMessageHandler;
    private Logger logger = LoggerFactory.getLogger(OperateRestController.class);

    @Autowired
    public OperateRestController(ServerWebMessageHandler serverWebMessageHandler) {
        this.serverWebMessageHandler = serverWebMessageHandler;
    }

    /**
     * 更新设备的信息
     *
     * @param webEmployee 欲更新的设备
     * @param groupId     设备组 ID
     * @param deviceId    设备 ID
     * @return 更新的状态
     */
    @RequestMapping(value = "/devices/{groupId}/{deviceId}", method = RequestMethod.POST)
    public String updateDevices(@ModelAttribute WebEmployee webEmployee, @PathVariable int groupId,
                                @PathVariable int deviceId) throws InterruptedException {

        List<IMessage> messages = handleEmployeeUpdate(webEmployee);
        if (serverWebMessageHandler.deployDeviceMsg(messages)) {
            return "success\n" + webEmployee;
        }
        return "error";
    }

    /**
     * 控制设备开锁
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "开锁成功"消息
     */
    @RequestMapping(value = "/unlock/{groupId}/{deviceId}", method = RequestMethod.GET)
    public String operateDeviceUnlock(@PathVariable int groupId, @PathVariable int deviceId) {
        if (serverWebMessageHandler.deployDeviceMsg(new WebMsgOperateBoxUnlock(groupId, deviceId))) {
            return "success";
        } else return "error";
    }


    /**
     * 部署万能卡号到设备
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "部署万能卡号成功"消息
     */
    @RequestMapping(value = "/freecard/{groupId}/{deviceId}", method = RequestMethod.GET)
    public String deployFreeCard(@PathVariable int groupId, @PathVariable int deviceId) {
        if (serverWebMessageHandler.deployFreeCard(groupId, deviceId)) {
            return "success";
        } else return "error";
    }


    /**
     * 用于 Spring MVC 的 RESTful API 处理服务，接收并处理 Web 消息
     *
     * @param webEmployee 员工「设备」信息 bean
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
