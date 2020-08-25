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
import cc.bitky.clusterdeviceplatform.server.server.repo.TcpFeedBackRepository;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.except.TcpFeedbackItem;
import cc.bitky.clusterdeviceplatform.server.utils.ResMsg;
import cc.bitky.clusterdeviceplatform.server.utils.WebUtil;

/**
 * 服务器数据处理模块
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/server/feedback/items")
public class FeedbackMsgController {

    private final ServerCenterProcessor centerProcessor;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public FeedbackMsgController(ServerCenterProcessor centerProcessor) {
        this.centerProcessor = centerProcessor;
    }

    @GetMapping("/exception")
    public ResMsg getFeedbackItemsException() {
        logger.info("/server/feedback/items/exception");
        long l1 = System.currentTimeMillis();
        List<TcpFeedbackItem> items = centerProcessor.getTcpFeedBackItems(TcpFeedBackRepository.ItemType.EXCEPTION);
        WebUtil.printTimeConsumed(l1, logger);
        return new ResMsg(items);
    }

    @GetMapping("timeout")
    public ResMsg getFeedbackItemsTimeout() {
        logger.info("/server/feedback/items/timeout");
        long l1 = System.currentTimeMillis();
        List<TcpFeedbackItem> items = centerProcessor.getTcpFeedBackItems(TcpFeedBackRepository.ItemType.TIMEOUT);
        WebUtil.printTimeConsumed(l1, logger);
        return new ResMsg(items);
    }

    @GetMapping("/clear")
    public ResMsg clearFeedbackItems() {
        logger.info("/server/feedback/items/clear");
        long l1 = System.currentTimeMillis();
        centerProcessor.getTcpFeedBackRepository().clearItems();
        WebUtil.printTimeConsumed(l1, logger);
        return new ResMsg("success");
    }
}
