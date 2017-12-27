package cc.bitky.clusterdeviceplatform.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;

@RestController
@RequestMapping(value = "/info/server")
public class InfoServerRestController {

    private final ServerWebProcessor serverProcessor;

    @Autowired
    public InfoServerRestController(ServerWebProcessor serverProcessor) {
        this.serverProcessor = serverProcessor;
    }
//    /**
//     * 获取正在活动的设备组
//     *
//     * @return 设备集合
//     */
//    @GetMapping("/devicegroup/activated")
//    public Flux<Integer> getDeviceGroupActivated() {
//        return serverProcessor.getDeviceGroupActivated();
//    }

    /**
     * 获取服务器状态
     *
     * @return 服务器状态
     */
    @GetMapping("/status")
    public String obtainServerStatus() {
        return "success";
    }

//    @RequestMapping(value = "/queueframe", method = RequestMethod.GET)
//    public QueueInfo obtainQueueFrame() {
//        return serverProcessor.obtainQueueFrame();
//    }

}
