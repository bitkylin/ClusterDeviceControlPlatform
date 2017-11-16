package cc.bitky.clusterdeviceplatform.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;

@RestController
@RequestMapping(value = "/info/devices")
public class InfoDevicesRestController {

    private final ServerWebProcessor serverProcessor;

    @Autowired
    public InfoDevicesRestController(ServerWebProcessor serverProcessor) {
        this.serverProcessor = serverProcessor;
    }

//    /**
//     * 从数据库中获取设备的集合
//     *
//     * @param groupId  设备组 ID
//     * @param deviceId 设备 ID
//     * @return 设备集合
//     */
//    @GetMapping("/devices/{groupId}/{deviceId}")
//    public Flux<Device> getDevices(@PathVariable int groupId, @PathVariable int deviceId) {
//        return serverProcessor.getDeviceInfo(groupId, deviceId);
//    }
//
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
    @GetMapping("/server/status")
    public String obtainServerStatus() {
        return "success";
    }
//
//    @RequestMapping(value = "/queueframe", method = RequestMethod.GET)
//    public QueueInfo obtainQueueFrame() {
//        return serverProcessor.obtainQueueFrame();
//    }
}
