package cc.bitky.clustermanage.db.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.bean.Employee;
import cc.bitky.clustermanage.db.repository.DeviceGroupRepository;
import cc.bitky.clustermanage.global.CommSetting;
import cc.bitky.clustermanage.server.message.CardType;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseStatus;

@Repository
public class KyDbPresenter {
    private final DeviceGroupRepository deviceGroupRepository;
    private final DbEmployeePresenter dbEmployeePresenter;
    private final DbSettingPresenter dbSettingPresenter;
    private final DbDevicePresenter dbDevicePresenter;
    private final DbRoutinePresenter dbRoutinePresenter;
    private final StringRedisTemplate stringRedisTemplate;
    private final Random random = new Random();
    private int maxGroupId = 0;

    private Logger logger = LoggerFactory.getLogger(KyDbPresenter.class);

    @Autowired
    public KyDbPresenter(DbRoutinePresenter dbRoutinePresenter, DbEmployeePresenter dbEmployeePresenter
            , DeviceGroupRepository deviceGroupRepository, DbDevicePresenter dbDevicePresenter
            , DbSettingPresenter dbSettingPresenter, StringRedisTemplate stringRedisTemplate) {
        this.dbRoutinePresenter = dbRoutinePresenter;
        this.deviceGroupRepository = deviceGroupRepository;
        this.dbSettingPresenter = dbSettingPresenter;
        this.dbDevicePresenter = dbDevicePresenter;
        this.dbEmployeePresenter = dbEmployeePresenter;
        this.stringRedisTemplate = stringRedisTemplate;
    }
    /**
     * 获得最大的设备组 ID
     *
     * @return 设备组的ID
     */
    public int obtainMaxDeviceGroupId() {
        return maxGroupId;
    }

    /**
     * 处理心跳包，用户更新设备组和设备的时间，必要时自动创建设备组
     *
     * @param tcpMsgHeartBeat 心跳包
     * @param autoCreate      自动在数据库中创建设备
     */
    private void handleMsgHeartBeat(IMessage tcpMsgHeartBeat, boolean autoCreate) {
        int groupId = tcpMsgHeartBeat.getGroupId();
        if (maxGroupId < groupId) maxGroupId = groupId;
        String exec = "deviceGroup-activated-" + groupId;
        stringRedisTemplate.opsForValue().set(exec, "1", 60, TimeUnit.SECONDS);
    }

    /**
     * 处理设备状态包
     *
     * @param tcpMsgResponseStatus 设备状态包
     * @param autoCreate           是否处于「自动创建」模式
     * @return 处理后的 Device。 null: 未找到指定的 Device 或 Device 无更新
     */
    public Device handleMsgDeviceStatus(TcpMsgResponseStatus tcpMsgResponseStatus, boolean autoCreate) {
        long l1 = System.currentTimeMillis();

        //处理心跳，更新设备组信息，「Debug 模式下生成所需的设备和设备组」
        handleMsgHeartBeat(tcpMsgResponseStatus, autoCreate);
        long l2 = System.currentTimeMillis();

        int status = -1;
        //更新设备的状态信息
        String s = stringRedisTemplate.opsForValue().get("deviceStatus-" + tcpMsgResponseStatus.getGroupId() + "-" + tcpMsgResponseStatus.getDeviceId());
        try {
            if (s != null)
                status = Integer.valueOf(s);
        } catch (NumberFormatException ignored) {
        }
        Device device = null;

        //Redis 中未获取到设备状态或状态已改变
        if (status == -1 || status != tcpMsgResponseStatus.getStatus())
            //从MongoDB中获取指定设备
            device = dbDevicePresenter.handleMsgDeviceStatus(tcpMsgResponseStatus);

        if (device == null) {
            if (status == tcpMsgResponseStatus.getStatus()) {
                //未重新获取设备
                logger.info("设备「" + tcpMsgResponseStatus.getGroupId() + ", " + tcpMsgResponseStatus.getDeviceId() + "」『"
                        + status + "->" + status + "』: 状态无更新");
            } else
                //其他情况
                logger.warn("设备(" + tcpMsgResponseStatus.getGroupId() + ", " + tcpMsgResponseStatus.getDeviceId() + ") 不存在，无法处理");
            return null;
        }

        //设备状态已改变
        if (device.getStatus() != -1) {
            String exec = "deviceStatus-" + tcpMsgResponseStatus.getGroupId() + "-" + tcpMsgResponseStatus.getDeviceId();
            stringRedisTemplate.opsForValue().set(exec, device.getStatus() + "", (600 + random.nextInt(1200)), TimeUnit.SECONDS);
        }

        //Redis未获取到设备且MongoDB中设备状态未改变
        if (device.getStatus() == -1) {
            logger.info("考勤表无更新");
            logger.info("时间耗费：" + (l2 - l1) + "ms; " + (System.currentTimeMillis() - l2) + "ms");
            return null;
        }
        long l3 = System.currentTimeMillis();

        //若设备对应的员工不存在，则自动创建员工
        if (device.getEmployeeObjectId() == null && autoCreate) {
            Employee employee = dbEmployeePresenter.createEmployee(device.getGroupId(), device.getDeviceId());
            if (employee == null) {
                logger.warn("未能创建员工");
                return null;
            }
            device.setEmployeeObjectId(employee.getId());
            dbDevicePresenter.updateDevice(device);
        }
        long l4 = System.currentTimeMillis();

        //根据设备中记录的考勤表索引，获取并更新员工的考勤表
        if (device.getEmployeeObjectId() != null) {
            dbRoutinePresenter.updateRoutineById(device.getEmployeeObjectId(), tcpMsgResponseStatus);
        } else {
            logger.info("无指定设备对应的员工和考勤表，且无法自动创建");
        }
        long l5 = System.currentTimeMillis();
        logger.info("时间耗费：" + (l2 - l1) + "ms; " + (l3 - l2) + "ms; " + (l4 - l3) + "ms; " + (l5 - l4) + "ms");
        return device;
    }


    /**
     * 获得所需的充电柜
     *
     * @param groupId 设备组 Id
     * @param boxId   设备 Id
     * @return 设备信息的集合
     */
    public List<Device> getDevices(int groupId, int boxId) {
        return dbDevicePresenter.getDevices(groupId, boxId);
    }
    /**
     * 设备初始化: 根据员工 objectId 获取员工信息
     *
     * @param objectId 员工 objectId
     * @return 通过员工 objectId 查询整合而成的信息 bean
     */
    public Employee obtainEmployeeByEmployeeObjectId(String objectId) {
        return dbEmployeePresenter.ObtainEmployeeByObjectId(objectId);
    }

    /**
     * 获取卡号的集合
     *
     * @return 卡号的集合
     */
    public String[] getCardArray(CardType card) {
        return dbSettingPresenter.getCardArray(card);
    }

    /**
     * 将卡号保存到数据库
     *
     * @param freecards 卡号的数组
     * @param card      卡号类型
     * @return 是否保存成功
     */
    public boolean saveCardNumber(String[] freecards, CardType card) {
        return dbSettingPresenter.saveCardArray(freecards, card);
    }

    /**
     * 检索数据库，给定的卡号是否匹配确认卡号
     *
     * @param cardNumber 待检索的卡号
     * @return 是否匹配确认卡号
     */
    public boolean marchConfirmCard(String cardNumber) {
        return dbSettingPresenter.marchConfirmCard(cardNumber);
    }

    /**
     * 「操作」更换新的设备，将设备的剩余充电次数恢复为最大值
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return 操作成功
     */
    public boolean devicesRenew(int groupId, int deviceId) {
        List<Device> devices = dbDevicePresenter.getDevices(groupId, deviceId);
        if (devices == null || devices.size() == 0) return false;
        devices.forEach(device -> {
            device.setRemainChargeTime(CommSetting.DEVICE_INIT_CHARGE_TIMES);
            dbDevicePresenter.updateDevice(device);
        });
        return true;
    }

    /**
     * 从数据库中获取正在活动的设备组
     *
     * @return 设备集合
     */
    public List<Integer> getDeviceGroupActivated() {
        List<Integer> list = new ArrayList<>();
        if (maxGroupId <= 0) return list;
        for (int i = 1; i <= maxGroupId; i++) {
            String exec = "deviceGroup-activated-" + i;
            String s = stringRedisTemplate.opsForValue().get(exec);
            if (s != null) list.add(i);
        }
        if (list.size() > 0)
            maxGroupId = list.get(list.size() - 1);

        return list;
    }
}
