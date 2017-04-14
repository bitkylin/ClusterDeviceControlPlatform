package cc.bitky.clustermanage.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.bean.Employee;
import cc.bitky.clustermanage.db.presenter.DbDeviceGroupPresenter;
import cc.bitky.clustermanage.db.presenter.DbDevicePresenter;
import cc.bitky.clustermanage.db.presenter.DbEmployeePresenter;

/**
 * 设备信息获取及处理控制器
 */
@RestController
@RequestMapping(value = "/info/devices")
public class DeviceInfoController {

    private final DbDeviceGroupPresenter dbDeviceGroupPresenter;
    private final DbEmployeePresenter dbEmployeePresenter;
    private final DbDevicePresenter dbDevicePresenter;
    private final WebMessageHandler webMessageHandler;

    private Logger logger = LoggerFactory.getLogger(DeviceInfoController.class);

    @Autowired
    public DeviceInfoController(DbDeviceGroupPresenter dbDeviceGroupPresenter
            , DbEmployeePresenter dbEmployeePresenter
            , DbDevicePresenter dbDevicePresenter
            , WebMessageHandler webMessageHandler) {
        this.dbDeviceGroupPresenter = dbDeviceGroupPresenter;
        this.dbEmployeePresenter = dbEmployeePresenter;
        this.dbDevicePresenter = dbDevicePresenter;
        this.webMessageHandler = webMessageHandler;
    }

    /**
     * 从数据库中获取设备的集合
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return 设备集合
     */
    @RequestMapping(value = "/{groupId}/{deviceId}", method = RequestMethod.GET)
    public List<Device> getDevices(@PathVariable int groupId, @PathVariable int deviceId) {
        return dbDeviceGroupPresenter.getDevices(groupId, deviceId);
    }

    /**
     * 更新设备的信息
     *
     * @param employee 欲更新的设备
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return 更新的状态
     */
    @RequestMapping(value = "/{groupId}/{deviceId}", method = RequestMethod.POST)
    public String updateDevices(@ModelAttribute Employee employee, @PathVariable int groupId,
                                @PathVariable int deviceId) throws InterruptedException {
        if (webMessageHandler.handleEmployeeUpdate(employee)) {
            return "success: " + employee;
        }
        return "error";
    }
}
