package cc.bitky.clusterdeviceplatform.server.web.spa.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.config.WorkStatus;
import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.server.ServerRunner;
import cc.bitky.clusterdeviceplatform.server.server.repo.DeviceStatusRepository;
import cc.bitky.clusterdeviceplatform.server.web.spa.data.bean.DeviceStatusItem;
import cc.bitky.clusterdeviceplatform.server.web.spa.data.bean.EmployeeCategory;
import cc.bitky.clusterdeviceplatform.server.web.spa.data.bean.EmployeeGatherByDepartment;
import cc.bitky.clusterdeviceplatform.server.web.spa.data.bean.EmployeeGatherByGroup;
import cc.bitky.clusterdeviceplatform.server.web.spa.data.bean.EmployeeGatherOutline;
import cc.bitky.clusterdeviceplatform.server.web.spa.utils.ResMsg;


/**
 * 服务器缓存设备状态信息按要求汇总
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/server/dataprocess/statusgather")
public class StatusGatherController {

    @Autowired
    private final ServerRunner centerProcessor;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public StatusGatherController(ServerRunner centerProcessor) {
        this.centerProcessor = centerProcessor;
    }

    @GetMapping("/rebuild")
    public ResMsg rebuildEmployeeStatus() {
        logger.info("/server/dataprocess/statusgather/rebuild");
        long l1 = System.currentTimeMillis();
        boolean isSuccess = centerProcessor.rebuildEmployeeStatus();
        long l2 = System.currentTimeMillis();
        logger.info("总耗时：" + (l2 - l1) + " ms");
        if (isSuccess) {
            return new ResMsg("success");
        }else {
            return new ResMsg("error");
        }
    }

    @GetMapping("/gather")
    public ResMsg gatherEmployeeStatus() {
        logger.info("/server/dataprocess/statusgather/gather");
        long l1 = System.currentTimeMillis();

        DeviceStatusRepository repository = centerProcessor.getCenterProcessor().getDeviceStatusRepository();
        List<EmployeeGatherByGroup> groupList = new ArrayList<>(DeviceSetting.MAX_GROUP_ID);
        for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
            List<DeviceStatusItem> items = repository.getEmployeeItemsRefByCoordinate(i);
            EmployeeCategory category = classifyDeviceStatusItems(items);
            groupList.add(new EmployeeGatherByGroup(i, items.size(), category));
        }

        Map<String, List<DeviceStatusItem>> listMap = repository.getDepartmentCategory();
        List<EmployeeGatherByDepartment> departmentList = new ArrayList<>(listMap.size());
        listMap.forEach((department, list) -> {
            EmployeeCategory category = classifyDeviceStatusItems(list);
            departmentList.add(new EmployeeGatherByDepartment(department, list.size(), category));
        });
        long l2 = System.currentTimeMillis();
        logger.info("总耗时：" + (l2 - l1) + " ms");
        return new ResMsg(new EmployeeGatherOutline(departmentList, groupList));
    }

    /**
     * 对设备状态集合进行分类
     *
     * @param items 设备状态集合
     * @return 分类结果
     */
    private EmployeeCategory classifyDeviceStatusItems(List<DeviceStatusItem> items) {
        int using = 0;
        int full = 0;
        int charging = 0;
        int uninit = 0;
        int otherCharge = 0;

        int normal = 0;
        int exception = 0;
        int chargingOutTime = 0;
        int workingOutTime = 0;

        for (DeviceStatusItem item : items) {
            switch (item.getChargeStatus()) {
                case ChargeStatus.UNINIT:
                    uninit++;
                    break;
                case ChargeStatus.USING:
                    using++;
                    break;
                case ChargeStatus.CHARGING:
                    charging++;
                    break;
                case ChargeStatus.FULL:
                    full++;
                    break;
                default:
                    otherCharge++;
                    break;
            }
            switch (item.getWorkStatus()) {
                case WorkStatus.NORMAL:
                    normal++;
                    break;
                case WorkStatus.CHARGING_TIME_OVER:
                    chargingOutTime++;
                    break;
                case WorkStatus.WORK_TIME_OVER:
                    workingOutTime++;
                    break;
                default:
                    exception++;
                    break;
            }
        }
        return new EmployeeCategory(using, full, charging, uninit, otherCharge, normal, exception, chargingOutTime, workingOutTime);
    }

    @GetMapping("/test")
    public String test() {
        return "success";
    }
}
