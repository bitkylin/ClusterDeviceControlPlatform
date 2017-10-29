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

import cc.bitky.clustermanage.server.bean.ServerWebMessageHandler;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployLedSetting;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployLedStop;
import cc.bitky.clustermanage.web.bean.LedSetting;

@RestController
@RequestMapping(value = "/led")
public class LedRestController {

    private final ServerWebMessageHandler serverWebMessageHandler;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public LedRestController(ServerWebMessageHandler serverWebMessageHandler) {
        this.serverWebMessageHandler = serverWebMessageHandler;
    }


    @RequestMapping(value = "/{groupId}/setting", method = RequestMethod.POST, consumes = "application/json")
    public String ledSetting(@PathVariable int groupId,
                             @RequestBody LedSetting ledSetting,
                             @RequestParam(defaultValue = "0") int maxgroupid) {
        if (!ledSetting.isEnabled())
            return "error";
        WebMsgDeployLedSetting led = new WebMsgDeployLedSetting(groupId, 120, ledSetting);
        if (serverWebMessageHandler.deployDeviceMsg(led, maxgroupid, false, true))
            return "success";
        return "error";
    }

    @RequestMapping(value = "/{groupId}/stop", method = RequestMethod.GET)
    public String ledStop(@PathVariable int groupId,
                          @RequestParam(defaultValue = "0") int maxgroupid) {
        if (serverWebMessageHandler.deployDeviceMsg(new WebMsgDeployLedStop(groupId, 120), maxgroupid, false, true))
            return "success";
        return "error";
    }
}
