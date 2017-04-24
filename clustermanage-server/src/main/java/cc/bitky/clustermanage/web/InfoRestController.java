package cc.bitky.clustermanage.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.server.bean.ServerWebMessageHandler;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.web.bean.WebEmployee;

/**
 * 设备信息获取及处理控制器
 */
@RestController
@RequestMapping(value = "/info")
public class InfoRestController {

    private final ServerWebMessageHandler serverWebMessageHandler;
    private final WebMessageHandler webMessageHandler;
    private Logger logger = LoggerFactory.getLogger(InfoRestController.class);

    @Autowired
    public InfoRestController(WebMessageHandler webMessageHandler, ServerWebMessageHandler serverWebMessageHandler) {
        this.webMessageHandler = webMessageHandler;
        this.serverWebMessageHandler = serverWebMessageHandler;
    }

    /**
     * 从数据库中获取设备的集合
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return 设备集合
     */
    @RequestMapping(value = "/devices/{groupId}/{deviceId}", method = RequestMethod.GET)
    public List<Device> getDevices(@PathVariable int groupId, @PathVariable int deviceId) {
        return serverWebMessageHandler.getDeviceInfo(groupId, deviceId);
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

        List<IMessage> messages = webMessageHandler.handleEmployeeUpdate(webEmployee);
        if (serverWebMessageHandler.deployDeviceMsg(messages)) {
            return "success: " + webEmployee;
        }
        return "error";
    }

    @RequestMapping(value = "/freecard/", method = RequestMethod.GET)
    public String obtainFreeCard() {

    }

    /**
     * 保存确认卡号到服务器
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "保存确认卡号成功"消息
     */
    @RequestMapping(value = "/comfirmcard/{groupId}/{deviceId}", method = RequestMethod.POST, consumes = "application/json")
    public String deployConfirmCard(@RequestBody long[] freecards, @PathVariable int groupId, @PathVariable int deviceId) {
        logger.info(freecards.length + "");
        return "success";
    }

    /**
     * 部署万能卡号
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "开锁成功"消息
     */
    @RequestMapping(value = "/freecard/{groupId}/{deviceId}", method = RequestMethod.POST, consumes = "application/json")
    public String deployFreeCard(@RequestBody long[] freecards, @PathVariable int groupId, @PathVariable int deviceId) {
        if (serverWebMessageHandler.deployFreeCard(groupId, deviceId)) {
            return "success";
        } else return "error";
    }
}
