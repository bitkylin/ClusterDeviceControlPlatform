package cc.bitky.clusterdeviceplatform.server.web.spa.feedbackmsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import cc.bitky.clusterdeviceplatform.server.server.ServerCenterProcessor;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.except.TcpFeedbackItem;
import cc.bitky.clusterdeviceplatform.server.web.spa.utils.ResMsg;

/**
 * 服务器数据处理模块
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/server/feedback/items")
public class FeedbackMsgController {
    private final ServerCenterProcessor webProcessor;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public FeedbackMsgController(ServerCenterProcessor webProcessor) {
        this.webProcessor = webProcessor;
    }

    @GetMapping("")
    public ResMsg getFeedbackItems() {
        logger.info("/server/feedback/items/get");
        long l1 = System.currentTimeMillis();
        List<TcpFeedbackItem> items = webProcessor.getTcpFeedBackItems();
        long l2 = System.currentTimeMillis();
        logger.info("耗时：" + (l2 - l1) + " ms");
        return new ResMsg(items);
    }

    @GetMapping("/clear")
    public ResMsg clearFeedbackItems() {
        logger.info("/server/feedback/items/clear");
        long l1 = System.currentTimeMillis();
        webProcessor.getTcpFeedBackRepository().clearItems();
        long l2 = System.currentTimeMillis();
        logger.info("耗时：" + (l2 - l1) + " ms");
        return new ResMsg("success");
    }
}
