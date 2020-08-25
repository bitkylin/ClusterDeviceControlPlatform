package cc.bitky.clusterdeviceplatform.server.web.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.messageutils.msg.controlcenter.led.MsgLedOn;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.controlcenter.led.MsgCodecLedOff;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.controlcenter.led.MsgCodecLedOn;
import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;
import cc.bitky.clusterdeviceplatform.server.pojo.client.WebLed;

@RestController
@RequestMapping(value = "/led")
public class LedRestController {
    private final ServerWebProcessor webProcessor;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public LedRestController(ServerWebProcessor webProcessor) {
        this.webProcessor = webProcessor;
    }

    @RequestMapping(value = "/{groupId}/setting", method = RequestMethod.POST, consumes = "application/json")
    public String ledSetting(@PathVariable int groupId,
                             @RequestBody WebLed webLed) {
        if (!webLed.isEnabled()) {
            return "error";
        }
        MsgLedOn msgLedOn = MsgCodecLedOn.create(groupId, 0, webLed.getPoint(), webLed.getDuration(), webLed.getBrightness(), webLed.getText());
        if (webProcessor.sendMessageGrouped(msgLedOn)) {
            return "success";
        }
        return "error";
    }

    @RequestMapping(value = "/{groupId}/stop", method = RequestMethod.GET)
    public String ledStop(@PathVariable int groupId) {
        if (webProcessor.sendMessageGrouped(MsgCodecLedOff.create(groupId))) {
            return "success";
        }
        return "error";
    }
}
