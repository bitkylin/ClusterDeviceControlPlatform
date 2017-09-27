package cc.bitky.ClusterManageServerTestIris;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/operate")
public class OperateRestController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 「操作」远程开锁 [虹膜方式]
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "开锁成功"消息
     */
    @RequestMapping(value = "/devices/unlock/iris/{groupId}/{deviceId}", method = RequestMethod.GET)
    public String operateDeviceUnlockByIris(@PathVariable int groupId, @PathVariable int deviceId) {
        if (groupId > ServerSetting.MAX_DEVICE_GROUP_SIZE
                || groupId <= 0
                || deviceId > ServerSetting.MAX_DEVICE_SIZE_EACH_GROUP
                || deviceId <= 0) {

            logger.warn("「虹膜模块接口调取异常」组号：" + groupId + ", 设备号：" + deviceId);
            return "error";
        }
        logger.info("「成功」组号：" + groupId + ", 设备号：" + deviceId);
        return "success";
    }
}
