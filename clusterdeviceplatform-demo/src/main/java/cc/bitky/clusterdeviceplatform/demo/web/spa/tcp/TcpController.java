package cc.bitky.clusterdeviceplatform.demo.web.spa.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.demo.tcp.statistic.channel.ChannelOutline;
import cc.bitky.clusterdeviceplatform.demo.web.spa.utils.ResMsg;

/**
 * 服务器数据处理模块
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/server/tcp")
public class TcpController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/outline")
    public ResMsg userInfo() {
        logger.info("/server/tcp/outline");
        long l1 = System.currentTimeMillis();
        ChannelOutline outline;
           outline = new KyRandom().create();

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
