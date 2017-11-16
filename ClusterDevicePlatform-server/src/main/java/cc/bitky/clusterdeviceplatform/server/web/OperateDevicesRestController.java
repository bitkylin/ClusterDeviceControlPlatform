package cc.bitky.clusterdeviceplatform.server.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.messageutils.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;
import cc.bitky.clusterdeviceplatform.server.web.bean.QueueDevice;

@RestController
@RequestMapping(value = "/operate/devices")
public class OperateDevicesRestController {

    private final ServerWebProcessor webProcessor;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public OperateDevicesRestController(ServerWebProcessor webProcessor) {
        this.webProcessor = webProcessor;
    }

    /**
     * 从数据库中获取并更新设备的信息
     *
     * @param groupId          设备组 ID
     * @param deviceId         设备 ID
     * @param name             是否更新姓名
     * @param department       是否更新部门
     * @param cardnumber       是否更新卡号
     * @param remainchargetime 是否更新剩余充电次数
     * @return 更新是否成功
     */
    @GetMapping("/devices/update/{groupId}/{deviceId}")
    public String updateDevices(@PathVariable int groupId,
                                @PathVariable int deviceId,
                                @RequestParam(required = false) boolean name,
                                @RequestParam(required = false) boolean department,
                                @RequestParam(required = false) boolean cardnumber,
                                @RequestParam(required = false) boolean remainchargetime) {
        if (webProcessor.queryAndDeployDeviceMsg(new QueueDevice(groupId, deviceId, name, department, cardnumber, remainchargetime))) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 「操作」远程开锁 [缺省方式]
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "开锁成功"消息
     */
    @GetMapping("/devices/unlock/default/{groupId}/{deviceId}")
    public String operateDeviceUnlockByDefault(@PathVariable int groupId,
                                               @PathVariable int deviceId) {
        QueueDevice queueDevice = new QueueDevice(groupId, deviceId);
        queueDevice.setUnLock(true);
        if (webProcessor.queryAndDeployDeviceMsg(queueDevice)) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 「操作」远程开锁 [虹膜方式]
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "开锁成功"消息
     */
    @GetMapping("/devices/unlock/iris/{groupId}/{deviceId}")
    public String operateDeviceUnlockByIris(@PathVariable int groupId, @PathVariable int deviceId) {
        if (groupId > DeviceSetting.MAX_GROUP_ID
                || groupId <= 0
                || deviceId > DeviceSetting.MAX_DEVICE_ID
                || deviceId <= 0) {

            logger.warn("「虹膜模块接口调取异常」设备定位信息，组号：" + groupId + ", 设备号：" + deviceId);
            return "error";
        }
        QueueDevice queueDevice = new QueueDevice(groupId, deviceId);
        queueDevice.setUnLock(true);
        if (webProcessor.queryAndDeployDeviceMsg(queueDevice)) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 「操作」将设备重置为出厂状态
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "初始化操作成功"消息
     */
    @GetMapping("/devices/reset/{groupId}/{deviceId}")
    public String operateDeviceReset(@PathVariable int groupId,
                                     @PathVariable int deviceId) {
        QueueDevice queueDevice = new QueueDevice(groupId, deviceId);
        queueDevice.setInitialize(true);
        if (webProcessor.queryAndDeployDeviceMsg(queueDevice)) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 「操作」更换新的设备,重置剩余充电次数
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "更新操作成功"消息
     */
    @GetMapping("/devices/renew/{groupId}/{deviceId}")
    public String operateDeviceRenew(@PathVariable int groupId,
                                     @PathVariable int deviceId) {
        QueueDevice queueDevice = new QueueDevice(groupId, deviceId);
        queueDevice.setRenew(true);
        if (webProcessor.queryAndDeployDeviceMsg(queueDevice)) {
            return "success";
        } else {
            return "error";
        }
    }
}
