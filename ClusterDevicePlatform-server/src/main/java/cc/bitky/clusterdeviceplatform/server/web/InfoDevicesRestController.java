package cc.bitky.clusterdeviceplatform.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;

@RestController
@RequestMapping(value = "/info/devices")
public class InfoDevicesRestController {

    private final ServerWebProcessor serverProcessor;

    @Autowired
    public InfoDevicesRestController(ServerWebProcessor serverProcessor) {
        this.serverProcessor = serverProcessor;
    }

    /**
     * 从数据库中获取设备的集合
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return 设备集合
     */
    @GetMapping("/{groupId}/{deviceId}")
    public List<Device> getDevices(@PathVariable int groupId, @PathVariable int deviceId) {
        return serverProcessor.getDbPresenter().queryDeviceInfo(groupId, deviceId);
    }
}
