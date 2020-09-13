package cc.bitky.clusterdeviceplatform.server.web.spa.data;

import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.config.WorkStatus;
import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.pojo.dataprocess.*;
import cc.bitky.clusterdeviceplatform.server.server.ServerRunner;
import cc.bitky.clusterdeviceplatform.server.server.repo.DeviceStatusRepository;
import cc.bitky.clusterdeviceplatform.server.utils.ResMsg;
import cc.bitky.clusterdeviceplatform.server.utils.WebUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 服务器缓存设备状态信息按要求汇总
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/server/dataprocess/statusgather")
public class StatusGatherController {

    private final ServerRunner centerProcessor;

    public StatusGatherController(ServerRunner centerProcessor) {
        this.centerProcessor = centerProcessor;
    }

    @GetMapping("/rebuild")
    public ResMsg rebuildEmployeeStatus() {
        log.info("/server/dataprocess/statusgather/rebuild");
        long l1 = System.currentTimeMillis();
        boolean isSuccess = centerProcessor.rebuildEmployeeStatus();
        WebUtil.printTimeConsumed(l1, log);
        if (isSuccess) {
            return new ResMsg("success");
        } else {
            return new ResMsg("error");
        }
    }

    @GetMapping("/gather")
    public ResMsg gatherEmployeeStatus() {
        log.info("/server/dataprocess/statusgather/gather");
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
        WebUtil.printTimeConsumed(l1, log);
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
