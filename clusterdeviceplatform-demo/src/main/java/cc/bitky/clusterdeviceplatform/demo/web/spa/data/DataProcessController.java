package cc.bitky.clusterdeviceplatform.demo.web.spa.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.demo.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.demo.db.statistic.pressure.GroupCacheItem;
import cc.bitky.clusterdeviceplatform.demo.db.statistic.status.DeviceGroupOutline;
import cc.bitky.clusterdeviceplatform.demo.web.spa.data.random.MsgCountRandom;
import cc.bitky.clusterdeviceplatform.demo.web.spa.utils.ResMsg;


/**
 * 服务器数据处理模块
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/server/dataprocess/devicegroup")
public class DataProcessController {
    MsgCountRandom countRandom;
    private Logger logger = LoggerFactory.getLogger(getClass());

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
        DeviceGroupOutline outline = KyRandom.createOutline();

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
    @GetMapping("/pressure")
    public ResMsg getDeviceGroupPressure() {
        logger.info("/server/dataprocess/devicegroup/pressure");
        GroupCacheItem item = null;
        item = GroupCacheItem.randomInstance();

        item.setAlarmLimit(100, 500);
        return new ResMsg(item);
    }

    @GetMapping("/detail/{groupId}")
    public ResMsg getDeviceByGroupId(@PathVariable int groupId) {
        logger.info("/server/dataprocess/devicegroup/detail/" + groupId);
        long l1 = System.currentTimeMillis();
        DeviceGroupOutline outline;
        outline = KyRandom.createDetail(groupId);

        outline.setAlarmLimit(10, 100);
        long l2 = System.currentTimeMillis();
        logger.info("耗时：" + (l2 - l1) + " ms");
        return new ResMsg(outline);
    }

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
