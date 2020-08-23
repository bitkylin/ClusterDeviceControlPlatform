package cc.bitky.clusterdeviceplatform.demo.web.spa.data;

import cc.bitky.clusterdeviceplatform.demo.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.demo.db.statistic.pressure.GroupCacheItem;
import cc.bitky.clusterdeviceplatform.demo.db.statistic.status.DeviceGroupOutline;
import cc.bitky.clusterdeviceplatform.demo.web.spa.data.random.KyRandom;
import cc.bitky.clusterdeviceplatform.demo.web.spa.utils.ResMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;


/**
 * 服务器数据处理模块
 *
 * @author limingliang
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/server/dataprocess/devicegroup")
public class DataProcessController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
        logger.info("耗时：{} ms", (l2 - l1));
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
        logger.info("/server/dataprocess/devicegroup/detail/{}", groupId);
        long l1 = System.currentTimeMillis();
        DeviceGroupOutline outline;
        outline = KyRandom.createDetail(groupId);

        outline.setAlarmLimit(10, 100);
        long l2 = System.currentTimeMillis();
        logger.info("耗时：{} ms", (l2 - l1));
        return new ResMsg(outline);
    }

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
