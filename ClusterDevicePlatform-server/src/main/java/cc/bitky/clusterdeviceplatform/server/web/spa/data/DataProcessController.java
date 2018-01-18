package cc.bitky.clusterdeviceplatform.server.web.spa.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.db.statistic.pressure.GroupCacheItem;
import cc.bitky.clusterdeviceplatform.server.db.statistic.status.DeviceGroupOutline;
import cc.bitky.clusterdeviceplatform.server.server.ServerCenterProcessor;
import cc.bitky.clusterdeviceplatform.server.web.spa.utils.ResMsg;

import static cc.bitky.clusterdeviceplatform.server.config.ServerSetting.WEB_RANDOM_DEBUG;


/**
 * 服务器数据处理模块
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/server/dataprocess")
public class DataProcessController {

    @Autowired
    ServerCenterProcessor webProcessor;
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 获取设备组的数量
     *
     * @return 返回消息对象
     */
    @GetMapping("/devicegroup/count")
    public ResMsg getDeviceGroupCount() {
        logger.info("/server/dataprocess/devicegroup/count");
        return new ResMsg(DeviceSetting.MAX_GROUP_ID);
    }

    @GetMapping("/devicegroup/outline")
    public ResMsg getDeviceGroupOutline() {
        logger.info("/server/dataprocess/devicegroup/outline");
        long l1 = System.currentTimeMillis();
        DeviceGroupOutline outline;
        if (WEB_RANDOM_DEBUG) {
            outline = KyRandom.createOutline();
        } else {
            outline = webProcessor.getRepository().createOutline();
        }
        outline.setAlarmLimit(10, 100);
        long l2 = System.currentTimeMillis();
        logger.info("耗时：" + (l2 - l1) + " ms");
        return new ResMsg(outline);
    }

    /**
     * 获取设备组的压力概览
     *
     * @return 返回消息对象
     */
    @GetMapping("/devicegroup/pressure")
    public ResMsg getDeviceGroupPressure() {
        logger.info("/server/dataprocess/devicegroup/pressure");
        GroupCacheItem item = webProcessor.getRepository().statisticChannelLoad();
        item.setAlarmLimit(100, 500);
        return new ResMsg(item);
    }

    @GetMapping("/device/detail/{groupId}")
    public ResMsg getDeviceByGroupId(@PathVariable int groupId) {
        logger.info("/server/dataprocess/device/detail/" + groupId);
        long l1 = System.currentTimeMillis();
        DeviceGroupOutline outline;
        if (WEB_RANDOM_DEBUG) {
            outline = KyRandom.createDetail(groupId);
        } else {
            outline = webProcessor.getRepository().createDetail(groupId);
        }
        outline.setAlarmLimit(10, 100);
        long l2 = System.currentTimeMillis();
        logger.info("耗时：" + (l2 - l1) + " ms");
        return new ResMsg(outline);
    }

    @GetMapping("/device/count")
    public ResMsg getDeviceCount() {
        return new ResMsg(DeviceSetting.MAX_DEVICE_ID);
    }

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
