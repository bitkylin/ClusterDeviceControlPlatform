package cc.bitky.clusterdeviceplatform.client.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.client.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.client.server.ServerWebHandler;
import cc.bitky.clusterdeviceplatform.client.server.statistic.MsgCount;
import cc.bitky.clusterdeviceplatform.client.server.statistic.ProcessedMsgRepo;


/**
 * 服务器数据处理模块
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/client/tcp")
public class TcpController {

    private final ServerWebHandler webHandler;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public TcpController(ServerWebHandler webHandler) {
        this.webHandler = webHandler;
    }


    @GetMapping("/clear")
    public String dataClear() {
        ProcessedMsgRepo.MSG_COUNT.set(0);
        ProcessedMsgRepo.MSG_CHARGE_COUNT.set(0);
        ProcessedMsgRepo.MSG_CHARGE_COUNT_FIXED.set(0);
        ProcessedMsgRepo.MSG_CHARGE_COUNT_VARIABLE.set(0);
        ProcessedMsgRepo.MSG_WORK_COUNT.set(0);
        ProcessedMsgRepo.MSG_WORK_COUNT_FIXED.set(0);
        ProcessedMsgRepo.MSG_WORK_COUNT_VARIABLE.set(0);
        return "success";
    }


    @GetMapping("/connectallchannel")
    public String connectAllChannel() {
        for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
            webHandler.getTcp().startClient(i);
        }
        return "success";
    }


    @GetMapping("/test1")
    public String test1() {
        webHandler.runTest1();
        return "success";
    }


    @GetMapping("/test2")
    public MsgCount test2() {
        return new MsgCount();
    }

}
