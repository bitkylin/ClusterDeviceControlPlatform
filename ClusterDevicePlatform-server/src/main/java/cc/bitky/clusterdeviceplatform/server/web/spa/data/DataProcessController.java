package cc.bitky.clusterdeviceplatform.server.web.spa.data;

import cc.bitky.clusterdeviceplatform.server.server.demonstrate.msgprocess.KyRandom;
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
import cc.bitky.clusterdeviceplatform.server.utils.ResMsg;
import cc.bitky.clusterdeviceplatform.server.utils.WebUtil;

import static cc.bitky.clusterdeviceplatform.server.config.ServerSetting.WEB_RANDOM_DEBUG;


/**
 * 服务器数据处理模块
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/server/dataprocess/devicegroup")
public class DataProcessController {

    private final ServerCenterProcessor webProcessor;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public DataProcessController(ServerCenterProcessor webProcessor) {
        this.webProcessor = webProcessor;
    }

    /**
     * 获取设备组的数量
     *
     * @return 返回消息对象
     */
    @GetMapping("/maxgroupcount")
    public ResMsg getDeviceGroupCount() {
        logger.info("/server/dataprocess/devicegroup/count");
        return new ResMsg(DeviceSetting.MAX_GROUP_ID);
    }

    @GetMapping("/maxdevicecount")
    public ResMsg getDeviceCount() {
        return new ResMsg(DeviceSetting.MAX_DEVICE_ID);
    }

    @GetMapping("/outline")
    public ResMsg getDeviceGroupOutline() {
        logger.info("/server/dataprocess/devicegroup/outline");
        long l1 = System.currentTimeMillis();
        DeviceGroupOutline outline;
        if (WEB_RANDOM_DEBUG) {
            outline = KyRandom.createOutline();
        } else {
            outline = webProcessor.getMsgProcessingRepository().createOutline();
        }
        outline.setAlarmLimit(10, 100);
        WebUtil.printTimeConsumed(l1, logger);
        return new ResMsg(outline);
    }

    /**
     * 获取设备组的压力概览
     *
     * @return 返回消息对象
     */
    @GetMapping("/pressure")
    public ResMsg getDeviceGroupPressure() {
        logger.info("/server/dataprocess/devicegroup/pressure");
        long l1 = System.currentTimeMillis();
        GroupCacheItem item = null;
        if (WEB_RANDOM_DEBUG) {
            item = GroupCacheItem.randomInstance();
        } else {
            item = webProcessor.getMsgProcessingRepository().statisticChannelLoad();
        }
        item.setAlarmLimit(100, 500);
        WebUtil.printTimeConsumed(l1, logger);
        return new ResMsg(item);
    }

    @GetMapping("/detail/{groupId}")
    public ResMsg getDeviceByGroupId(@PathVariable int groupId) {
        logger.info("/server/dataprocess/devicegroup/detail/" + groupId);
        long l1 = System.currentTimeMillis();
        DeviceGroupOutline outline;
        if (WEB_RANDOM_DEBUG) {
            outline = KyRandom.createDetail(groupId);
        } else {
            outline = webProcessor.getMsgProcessingRepository().createDetail(groupId);
        }
        outline.setAlarmLimit(10, 100);
        WebUtil.printTimeConsumed(l1, logger);
        return new ResMsg(outline);
    }

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
