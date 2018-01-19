package cc.bitky.clusterdeviceplatform.server.web.spa.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.channel.ChannelOutline;
import cc.bitky.clusterdeviceplatform.server.web.spa.utils.ResMsg;

import static cc.bitky.clusterdeviceplatform.server.config.ServerSetting.WEB_RANDOM_DEBUG;


/**
 * 服务器数据处理模块
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/server/tcp")
public class TcpController {

    private final ServerWebProcessor serverProcessor;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public TcpController(ServerWebProcessor serverProcessor) {
        this.serverProcessor = serverProcessor;
    }

    @GetMapping("/outline")
    public ResMsg userInfo() {
        logger.info("/server/tcp/outline");
        long l1 = System.currentTimeMillis();
        ChannelOutline outline;
        if (WEB_RANDOM_DEBUG) {
            outline = new KyRandom().create();
        } else {
            outline = serverProcessor.getTcpProcessor().statisticChannelLoad();
        }
        outline.setAlarmLimit(100, 500);
        long l2 = System.currentTimeMillis();
        logger.info("耗时：" + (l2 - l1) + " ms");
        return new ResMsg(outline);
    }


    @GetMapping("/test")
    public String test() {
        return "success";
    }

}
