package cc.bitky.clusterdeviceplatform.server.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msg.MsgReplyDeviceStatus;
import cc.bitky.clusterdeviceplatform.server.db.bean.CardSet;
import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.db.bean.Employee;
import cc.bitky.clusterdeviceplatform.server.db.operate.CardSetOperate;
import cc.bitky.clusterdeviceplatform.server.db.operate.DbRoutineOperate;
import cc.bitky.clusterdeviceplatform.server.db.operate.DeviceOperate;
import cc.bitky.clusterdeviceplatform.server.db.operate.DeviceStatusRepo;
import cc.bitky.clusterdeviceplatform.server.db.operate.EmployeeOperate;
import cc.bitky.clusterdeviceplatform.server.web.bean.CardType;
import reactor.core.publisher.Mono;

@Service
public class DbPresenter {

    private final CardSetOperate cardSetOperate;
    private final DeviceOperate deviceOperate;
    private final EmployeeOperate employeeOperate;
    private final DeviceStatusRepo deviceStatusRepo;
    private final DbRoutineOperate dbRoutineOperate;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public DbPresenter(CardSetOperate cardSetOperate, DeviceOperate deviceOperate, EmployeeOperate employeeOperate, DeviceStatusRepo deviceStatusRepo, DbRoutineOperate dbRoutineOperate) {
        this.cardSetOperate = cardSetOperate;
        this.deviceOperate = deviceOperate;
        this.employeeOperate = employeeOperate;
        this.deviceStatusRepo = deviceStatusRepo;
        this.dbRoutineOperate = dbRoutineOperate;
    }

    /**
     * 保存特定的卡号组
     *
     * @param cards 特定的卡号组
     * @param type  卡号组的类型
     * @return 保存成功
     */
    public Mono<CardSet> saveCardSet(String[] cards, CardType type) {
        return cardSetOperate.saveCardSet(type.name(), cards);
    }

    /**
     * 根据类型获取指定的卡号组
     *
     * @param type 卡号组的类型
     * @return 特定的卡号组
     */
    public Mono<String[]> queryCardSet(CardType type) {
        return Mono.just(cardSetOperate.obtainCardSet(type.name()).block().getCardList());

    }

    /**
     * 获取设备的集合
     *
     * @param groupId  设备组 Id
     * @param deviceId 设备 Id
     * @return 设备的集合
     */
    public List<Device> queryDeviceInfo(int groupId, int deviceId) {
        return deviceOperate.queryDeviceInfo(groupId, deviceId);
    }

    /**
     * 保存特定的设备
     *
     * @param device 特定的设备
     * @return 特定的设备
     */
    public Device saveDeviceInfo(Device device) {
        return deviceOperate.saveDeviceInfo(device);
    }

    /**
     * 根据 ObjectId 检索指定的员工对象
     *
     * @param objectId 员工的 ObjectId
     * @return 指定的员工对象
     */
    public Optional<Employee> queryEmployee(String objectId) {
        return employeeOperate.queryEmployee(objectId);
    }

    /**
     * 处理设备状态包
     *
     * @param msgStatus 设备状态包
     * @return 处理后的 Device。 null: 未找到指定的 Device 或 Device 无更新
     */
    public Device handleMsgDeviceStatus(MsgReplyDeviceStatus msgStatus) {
        long l1 = System.currentTimeMillis();
        //比较服务器缓存，是否状态更新，未更新直接 return
        int status = deviceStatusRepo.getStatus(msgStatus.getGroupId(), msgStatus.getDeviceId(), msgStatus.getType());
        if (status == msgStatus.getStatus()) {
            logger.info("设备「" + msgStatus.getGroupId() + ", " + msgStatus.getDeviceId() + "」『"
                    + status + "->" + status + "』: 状态无更新 [" + msgStatus.getType().name() + "]");
            return null;
        } else {
            deviceStatusRepo.setStatus(msgStatus.getGroupId(), msgStatus.getDeviceId(), msgStatus.getStatus(), msgStatus.getType());
        }

        //状态未更新
        Device device;
        if (msgStatus.getType() == MsgReplyDeviceStatus.Type.CHARGE) {
            device = deviceOperate.handleChargeStatus(msgStatus);
        } else {
            device = deviceOperate.handleWorkStatus(msgStatus);
        }
        if (device == null || device.getChargeStatus() == ChargeStatus.FRAME_EXCEPTION) {
            return null;
        }

        //设备状态已改变
        long l2 = System.currentTimeMillis();
        //根据设备中记录的考勤表索引，获取并更新员工的考勤表
        if (device.getEmployeeObjectId() != null) {
            dbRoutineOperate.updateRoutineById(device.getEmployeeObjectId(), msgStatus, msgStatus.getType());
        } else {
            logger.info("无指定设备对应的员工，故未更新考勤表");
        }
        long l3 = System.currentTimeMillis();
        logger.info("时间耗费：" + (l2 - l1) + "ms; " + (l3 - l2) + "ms");
        return device;
    }
}
