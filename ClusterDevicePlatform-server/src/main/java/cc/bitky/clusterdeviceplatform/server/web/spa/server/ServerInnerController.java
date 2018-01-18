package cc.bitky.clusterdeviceplatform.server.web.spa.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.server.db.statistic.repo.ProcessedMsgRepo;
import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;
import cc.bitky.clusterdeviceplatform.server.server.statistic.CollectInfo;
import cc.bitky.clusterdeviceplatform.server.server.statistic.utils.Test;
import cc.bitky.clusterdeviceplatform.server.web.spa.utils.ResMsg;

/**
 * 服务器数据处理模块
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/server/inner")
public class ServerInnerController {
    private final ServerWebProcessor serverProcessor;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ServerInnerController(ServerWebProcessor serverProcessor) {
        this.serverProcessor = serverProcessor;
    }

    @GetMapping("/outline")
    public ResMsg serverOutline() {
        logger.info("/server/inner/outline");
        long l1 = System.currentTimeMillis();
        CollectInfo collectInfo = new CollectInfo();
        long l2 = System.currentTimeMillis();
        logger.info("耗时：" + (l2 - l1) + " ms");
        return new ResMsg(collectInfo);
    }

    @GetMapping("/test")
    public ResMsg printTest() {
        Test.main(new String[0]);
        return new ResMsg("success");
    }

    @GetMapping("/clear")
    public ResMsg cacheDataClear() {
        ProcessedMsgRepo.MSG_COUNT.set(0);
        ProcessedMsgRepo.MSG_CHARGE_COUNT.set(0);
        ProcessedMsgRepo.MSG_CHARGE_COUNT_FIXED.set(0);
        ProcessedMsgRepo.MSG_CHARGE_COUNT_VARIABLE.set(0);
        ProcessedMsgRepo.MSG_WORK_COUNT.set(0);
        ProcessedMsgRepo.MSG_WORK_COUNT_FIXED.set(0);
        ProcessedMsgRepo.MSG_WORK_COUNT_VARIABLE.set(0);
        return new ResMsg("success");
    }

    @GetMapping("/attach")
    public ResMsg serverAttach() {
        return new ResMsg("success");
    }
}
