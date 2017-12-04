package cc.bitky.clusterdeviceplatform.server.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import cc.bitky.clusterdeviceplatform.messageutils.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecDeviceClear;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecDeviceRemainChargeTimes;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecDeviceUnlock;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecEmployeeDepartment;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecEmployeeName;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardEmployee;
import cc.bitky.clusterdeviceplatform.server.config.CommSetting;
import cc.bitky.clusterdeviceplatform.server.config.WebSetting;
import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.db.bean.Employee;
import cc.bitky.clusterdeviceplatform.server.db.repository.EmployeeRepository;
import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;
import cc.bitky.clusterdeviceplatform.server.web.bean.QueueDevice;

@RestController
@RequestMapping(value = "/operate/devices")
public class OperateDevicesRestController {

    private final ServerWebProcessor webProcessor;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    EmployeeRepository repository;

    @Autowired
    public OperateDevicesRestController(ServerWebProcessor webProcessor, EmployeeRepository repository) {
        this.webProcessor = webProcessor;
        this.repository = repository;
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
    @GetMapping("/update/{groupId}/{deviceId}")
    public String updateDevices(@PathVariable int groupId,
                                @PathVariable int deviceId,
                                @RequestParam(required = false) boolean name,
                                @RequestParam(required = false) boolean department,
                                @RequestParam(required = false) boolean cardnumber,
                                @RequestParam(required = false) boolean remainchargetime) {
        if (queryAndDeployDeviceMsg(new QueueDevice(groupId, deviceId, name, department, cardnumber, remainchargetime))) {
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
    @GetMapping("/unlock/default/{groupId}/{deviceId}")
    public String operateDeviceUnlockByDefault(@PathVariable int groupId,
                                               @PathVariable int deviceId) {
        if (webProcessor.sendMessageGrouped(MsgCodecDeviceUnlock.create(groupId, deviceId))) {
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
    @GetMapping("/unlock/iris/{groupId}/{deviceId}")
    public String operateDeviceUnlockByIris(@PathVariable int groupId, @PathVariable int deviceId) {
        if (groupId > DeviceSetting.MAX_GROUP_ID
                || groupId <= 0
                || deviceId > DeviceSetting.MAX_DEVICE_ID
                || deviceId <= 0) {

            logger.warn("「虹膜模块接口调取异常」设备定位信息，组号：" + groupId + ", 设备号：" + deviceId);
            return "error";
        }
        if (webProcessor.sendMessageGrouped(MsgCodecDeviceUnlock.create(groupId, deviceId))) {
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
    @GetMapping("/reset/{groupId}/{deviceId}")
    public String operateDeviceReset(@PathVariable int groupId,
                                     @PathVariable int deviceId) {
        if (webProcessor.sendMessageGrouped(MsgCodecDeviceClear.create(groupId, deviceId))) {
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
    @GetMapping("/replace/{groupId}/{deviceId}")
    public String operateDeviceReplace(@PathVariable int groupId,
                                       @PathVariable int deviceId) {
        QueueDevice queueDevice = new QueueDevice(groupId, deviceId);
        queueDevice.setReplace(true);
        if (queryAndDeployDeviceMsg(queueDevice)) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 根据特定条件，检索并向设备部署相应的消息对象
     *
     * @param queue 特定的部署条件
     * @return 部署成功
     */
    private boolean queryAndDeployDeviceMsg(QueueDevice queue) {
        if (queue.getGroupId() == WebSetting.BROADCAST_GROUP_ID) {
            for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
                webProcessor.getDbPresenter().queryDeviceInfo(i, queue.getDeviceId()).forEach(device -> deployEmployeeMsg(device, queue));
            }
        } else {
            webProcessor.getDbPresenter().queryDeviceInfo(queue.getGroupId(), queue.getDeviceId()).forEach(device -> deployEmployeeMsg(device, queue));
        }
        return true;
    }

    /**
     * 根据特定条件，检索并向指定设备部署相应的消息对象
     *
     * @param device 特定的设备
     * @param queue  特定的部署条件
     */
    private void deployEmployeeMsg(Device device, QueueDevice queue) {
        if (queue.isName() || queue.isDepartment()) {
            Optional<Employee> optional = webProcessor.getDbPresenter().queryEmployee(device.getEmployeeObjectId());
            if (optional.isPresent() && queue.isName()) {
                webProcessor.sendMessageGrouped(MsgCodecEmployeeName.create(device.getGroupId(), device.getDeviceId(), optional.get().getName()));
            }
            if (optional.isPresent() && queue.isDepartment()) {
                webProcessor.sendMessageGrouped(MsgCodecEmployeeDepartment.create(device.getGroupId(), device.getDeviceId(), optional.get().getDepartment()));
            }
        }
        if (queue.isCard()) {
            webProcessor.sendMessageGrouped(MsgCodecCardEmployee.create(device.getGroupId(), device.getDeviceId(), device.getCardNumber()));
        }
        if (queue.isRemainChargeTime()) {
            webProcessor.sendMessageGrouped(MsgCodecDeviceRemainChargeTimes.create(device.getGroupId(), device.getDeviceId(), device.getRemainChargeTime()));
        }
        if (queue.isReplace()) {
            device.setRemainChargeTime(CommSetting.DEVICE_INIT_CHARGE_TIMES);
            webProcessor.getDbPresenter().saveDeviceInfo(device);
        }
    }
}
