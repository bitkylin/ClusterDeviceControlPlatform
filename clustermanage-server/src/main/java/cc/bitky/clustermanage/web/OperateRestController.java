package cc.bitky.clustermanage.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import cc.bitky.clustermanage.server.bean.ServerWebMessageHandler;
import cc.bitky.clustermanage.server.message.CardType;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.server.message.web.WebMsgInitClearDeviceStatus;
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
     * 从数据库中获取并更新设备的信息
     *
     * @param groupId    设备组 ID
     * @param deviceId   设备 ID
     * @param name       是否更新姓名
     * @param department 是否更新部门
     * @param cardnumber 是否更新卡号
     * @param maxgroupId 若更新多个设备组，可指定更新设备组的 ID 范围为: 1 - maxgroupId
     * @return 更新是否成功
     */
    @RequestMapping(value = "/devices/update/{groupId}/{deviceId}", method = RequestMethod.GET)
    public String updateDevices(@PathVariable int groupId,
                                @PathVariable int deviceId,
                                @RequestParam(required = false) boolean name,
                                @RequestParam(required = false) boolean department,
                                @RequestParam(required = false) boolean cardnumber,
                                @RequestParam(defaultValue = "0") int maxgroupId) {
        if (serverWebMessageHandler.obtainDeployDeviceMsg(groupId, deviceId, name, department, cardnumber, maxgroupId))
            return "success";
        else return "error";
    }


    /**
     * 「操作」远程开锁
     *
     * @param groupId    设备组 ID
     * @param deviceId   设备 ID
     * @param maxgroupId 最大设备组号
     * @return "开锁成功"消息
     */
    @RequestMapping(value = "/devices/unlock/{groupId}/{deviceId}", method = RequestMethod.GET)
    public String operateDeviceUnlock(@PathVariable int groupId,
                                      @PathVariable int deviceId,
                                      @RequestParam(defaultValue = "0") int maxgroupId) {
        if (serverWebMessageHandler.deployDeviceMsg(new WebMsgOperateBoxUnlock(groupId, deviceId), maxgroupId)) {
            return "success";
        } else return "error";
    }

    /**
     * 「操作」将设备重置为出厂状态
     *
     * @param groupId    设备组 ID
     * @param deviceId   设备 ID
     * @param maxgroupId 最大设备组号
     * @return "初始化操作成功"消息
     */
    @RequestMapping(value = "/devices/reset/{groupId}/{deviceId}", method = RequestMethod.GET)
    public String operateDeviceReset(@PathVariable int groupId,
                                      @PathVariable int deviceId,
                                      @RequestParam(defaultValue = "0") int maxgroupId) {
        if (serverWebMessageHandler.deployDeviceMsg(new WebMsgInitClearDeviceStatus(groupId, deviceId), maxgroupId)) {
            return "success";
        } else return "error";
    }


    /**
     * 更新万能卡号到数据库并部署
     *
     * @param groupId   组号
     * @param deviceId  设备号
     * @param freeCards freeCards 万能卡号数组
     * @return "更新并部署万能卡号成功"消息
     */
    @RequestMapping(value = "/freecard/{groupId}/{deviceId}", method = RequestMethod.POST, consumes = "application/json")
    public String saveFreeCard(@PathVariable int groupId,
                               @PathVariable int deviceId,
                               @RequestBody long[] freeCards,
                               @RequestParam(defaultValue = "0") int maxgroupId) {
        if (serverWebMessageHandler.saveCardNumber(freeCards, CardType.FREE) &&
                serverWebMessageHandler.deployFreeCard(groupId, deviceId, maxgroupId))
            return "success";
        return "error";
    }

    /**
     * 用于 Spring MVC 的 RESTful API 处理服务，接收并处理 Web 消息
     *
     * @param webEmployee 员工「设备」信息 bean
     * @return 是否更新成功
     */
    private List<IMessage> handleEmployeeUpdate(WebEmployee webEmployee) {
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
