package cc.bitky.clusterdeviceplatform.server.web.spa.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.server.server.ServerCenterProcessor;
import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.channel.ChannelOutline;
import cc.bitky.clusterdeviceplatform.server.web.spa.tcp.bean.BaseMsgSending;
import cc.bitky.clusterdeviceplatform.server.web.spa.utils.ResMsg;
import cc.bitky.clusterdeviceplatform.server.web.spa.utils.WebUtil;

import static cc.bitky.clusterdeviceplatform.server.config.ServerSetting.WEB_RANDOM_DEBUG;


/**
 * 服务器数据处理模块
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/server/tcp")
public class TcpController {

    private final ServerWebProcessor webProcessor;
    private final ServerCenterProcessor centerProcessor;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public TcpController(ServerWebProcessor webProcessor, ServerCenterProcessor centerProcessor) {
        this.webProcessor = webProcessor;
        this.centerProcessor = centerProcessor;
    }

    @GetMapping("/outline")
    public ResMsg tcpOutline() {
        logger.info("/server/tcp/outline");
        long l1 = System.currentTimeMillis();
        ChannelOutline outline;
        if (WEB_RANDOM_DEBUG) {
            outline = new KyRandom().create();
        } else {
            outline = webProcessor.getTcpProcessor().statisticChannelLoad();
        }
        outline.setAlarmLimit(100, 500);
        WebUtil.printTimeConsumed(l1, logger);
        return new ResMsg(outline);
    }


    @GetMapping("/msg/sending/{groupId}/{deviceId}")
    public ResMsg msgSending(@PathVariable int groupId,
                             @PathVariable int deviceId,
                             @RequestParam(required = false, defaultValue = "false") boolean msgflat,
                             @RequestParam(required = false, defaultValue = "100") int msglimit) {
        logger.info("/server/tcp/msg/sending/" + groupId + "/" + deviceId + "?msgFlat=" + msgflat + " & msgLimit=" + msglimit);
        long l1 = System.currentTimeMillis();
        BaseMsgSending msgSending = centerProcessor.getMsgSendingOutline(groupId, deviceId, msgflat, msglimit);
        WebUtil.printTimeConsumed(l1, logger);
        return new ResMsg(msgSending);
    }

    @GetMapping("/test")
    public String test() {
        return "success";
    }

}
