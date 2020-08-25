package cc.bitky.clusterdeviceplatform.server.web.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

import javax.imageio.ImageIO;

import cc.bitky.clusterdeviceplatform.messageutils.config.FrameSetting;
import cc.bitky.clusterdeviceplatform.messageutils.define.utils.KyToArrayUtil;
import cc.bitky.clusterdeviceplatform.messageutils.msg.device.MsgEmployeePortrait;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardEmployee;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.controlcenter.MsgCodecTimestamp;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceClear;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceEnabled;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceRemainChargeTimes;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceSetEngineRotateDuration;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceSetOvertimeCharge;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceSetOvertimeDoorOpened;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceSetOvertimeWork;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceSetScreenBrightness;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceSetSoundStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceUnlock;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecEmployeeDepartment;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecEmployeeName;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecEmployeePortrait;
import cc.bitky.clusterdeviceplatform.server.config.CommSetting;
import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.config.WebSetting;
import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.db.bean.Employee;
import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;
import cc.bitky.clusterdeviceplatform.server.server.utils.DeviceOutBoundDetect;
import cc.bitky.clusterdeviceplatform.server.pojo.client.QueueDevice;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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
    @GetMapping("/update/{groupId}/{deviceId}")
    public String updateDevices(@PathVariable int groupId,
                                @PathVariable int deviceId,
                                @RequestParam(required = false, defaultValue = "false") boolean name,
                                @RequestParam(required = false, defaultValue = "false") boolean department,
                                @RequestParam(required = false, defaultValue = "false") boolean cardnumber,
                                @RequestParam(required = false, defaultValue = "false") boolean enabled,
                                @RequestParam(required = false, defaultValue = "false") boolean remainchargetime) {
        if (queryAndDeployDeviceMsg(new QueueDevice(groupId, deviceId, name, department, cardnumber, enabled, remainchargetime))) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 更新设备时间为当前系统时间
     *
     * @param groupId 设备组 ID
     * @return 更新是否成功
     */
    @GetMapping("/update/timestamp/current/{groupId}")
    public String updateUnixTimestamp(@PathVariable int groupId) {
        if (webProcessor.sendMessageGrouped(MsgCodecTimestamp.create(groupId))) {
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
        if (DeviceOutBoundDetect.detect(groupId, deviceId)) {
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
//        QueueDevice queueDevice = new QueueDevice(groupId, deviceId);
//        queueDevice.setReplace(true);
//        if (queryAndDeployDeviceMsg(queueDevice)) {
        return "已废弃";
//        } else {
//            return "error";
//        }
    }

    /**
     * 「操作」部署员工肖像
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @param width    图片宽度
     * @param height   图片高度
     * @param file     文件
     * @return "更新操作成功"消息
     * @throws IOException 图片读取异常
     */
    @PostMapping("/portrait/{groupId}/{deviceId}")
    public String operateDeviceDeployPortrait(@PathVariable int groupId,
                                              @PathVariable int deviceId,
                                              @RequestParam(required = false, defaultValue = "120") int width,
                                              @RequestParam(required = false, defaultValue = "160") int height,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        if (width * height > FrameSetting.EMPLOYEE_PORTRAIT_MAX_PIXEL_NUM) {
            return "「失败」设置的图片分辨率过大";
        }
        byte[] bytes = file.getBytes();
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        short[] shorts = KyToArrayUtil.photoToArray(image, 120, 160);
        ByteBuf byteBuf = Unpooled.buffer(65536);
        for (short i : shorts) {
            byteBuf.writeShortLE(i);
        }
        logger.info("byteBuf:" + byteBuf.readableBytes());
        int id = 0;
        while (byteBuf.isReadable()) {
            int length = byteBuf.readableBytes() > FrameSetting.EMPLOYEE_PORTRAIT_SEGMENT_SIZE ?
                    FrameSetting.EMPLOYEE_PORTRAIT_SEGMENT_SIZE : byteBuf.readableBytes();
            byte[] segmentArray = new byte[length];
            byteBuf.readBytes(segmentArray, 0, length);
            MsgEmployeePortrait msg = MsgCodecEmployeePortrait.create(groupId, deviceId, id++, segmentArray);
            if (!webProcessor.sendMessageGrouped(msg)) {
                return "error";
            }
        }
        return "success";
    }

    /**
     * 「操作设备」设置工作超时时间
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "设置成功"消息
     */
    @GetMapping("/option/overtime/work/{groupId}/{deviceId}")
    public String operateDeviceSetOverTimeWork(@PathVariable int groupId,
                                               @PathVariable int deviceId,
                                               @RequestParam(required = false, defaultValue = "1") int hour,
                                               @RequestParam(required = false, defaultValue = "0") int minute) {
        if (webProcessor.sendMessageGrouped(MsgCodecDeviceSetOvertimeWork.create(groupId, deviceId, hour, minute))) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 「操作」设置充电超时时间
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "设置成功"消息
     */
    @GetMapping("/option/overtime/charge/{groupId}/{deviceId}")
    public String operateDeviceSetOverTimeCharge(@PathVariable int groupId,
                                                 @PathVariable int deviceId,
                                                 @RequestParam(required = false, defaultValue = "1") int hour,
                                                 @RequestParam(required = false, defaultValue = "0") int minute) {
        if (webProcessor.sendMessageGrouped(MsgCodecDeviceSetOvertimeCharge.create(groupId, deviceId, hour, minute))) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 「操作」设备设置电机转动时间
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "设置成功"消息
     */
    @GetMapping("/option/engine/rotate/{groupId}/{deviceId}")
    public String operateDeviceSetEngineRotateDuration(@PathVariable int groupId,
                                                       @PathVariable int deviceId,
                                                       @RequestParam(required = false, defaultValue = "0") int value1,
                                                       @RequestParam(required = false, defaultValue = "0") int value2) {
        if (webProcessor.sendMessageGrouped(MsgCodecDeviceSetEngineRotateDuration.create(groupId, deviceId, value1, value2))) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 「操作设备」设置屏幕亮度
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "设置成功"消息
     */
    @GetMapping("/option/screen/lighteness/{groupId}/{deviceId}")
    public String operateDeviceSetScreenBrightness(@PathVariable int groupId,
                                                   @PathVariable int deviceId,
                                                   @RequestParam int intensity) {
        if (webProcessor.sendMessageGrouped(MsgCodecDeviceSetScreenBrightness.create(groupId, deviceId, intensity))) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 「操作设备」更新开门超时时间
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "设置成功"消息
     */
    @GetMapping("/option/overtime/dooropened/{groupId}/{deviceId}")
    public String operateDeviceSetOvertimeDoorOpened(@PathVariable int groupId,
                                                     @PathVariable int deviceId,
                                                     @RequestParam int time) {
        if (webProcessor.sendMessageGrouped(MsgCodecDeviceSetOvertimeDoorOpened.create(groupId, deviceId, time))) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 「操作设备」更新设备语音提醒状态
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return "设置成功"消息
     */
    @GetMapping("/option/status/sound/{groupId}/{deviceId}")
    public String operateDeviceSetSoundStatus(@PathVariable int groupId,
                                              @PathVariable int deviceId,
                                              @RequestParam boolean open) {
        if (webProcessor.sendMessageGrouped(MsgCodecDeviceSetSoundStatus.create(groupId, deviceId, open))) {
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
            Optional<Employee> optional = webProcessor.getDbPresenter().getEmployeeOperate().queryEmployee(device.getEmployeeObjectId());
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
            int remainChargeTime = device.getRemainChargeTime();
            if (remainChargeTime > CommSetting.DEPLOY_REMAIN_CHARGE_TIMES) {
                remainChargeTime = CommSetting.REMAIN_CHARGE_TIMES_CLEAR;
            }
            webProcessor.sendMessageGrouped(MsgCodecDeviceRemainChargeTimes.create(device.getGroupId(), device.getDeviceId(), remainChargeTime));
        }
//        if (queue.isReplace()) {
//            device.setRemainChargeTime(CommSetting.DEVICE_INIT_CHARGE_TIMES);
//            webProcessor.getDbPresenter().saveDeviceInfo(device);
//        }
        webProcessor.sendMessageGrouped(MsgCodecDeviceEnabled.create(device.getGroupId(), device.getDeviceId(), queue.isEnabled()));
    }
}
