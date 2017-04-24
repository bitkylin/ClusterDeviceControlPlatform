package cc.bitky.clustermanage.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clustermanage.server.bean.ServerWebMessageHandler;
import cc.bitky.clustermanage.server.message.web.WebMsgOperateBoxUnlock;

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
}
