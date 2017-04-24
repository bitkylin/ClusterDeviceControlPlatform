package cc.bitky.clustermanage.db.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.bean.DeviceGroup;
import cc.bitky.clustermanage.db.bean.Employee;
import cc.bitky.clustermanage.db.repository.DeviceGroupRepository;
import cc.bitky.clustermanage.server.bean.ServerWebMessageHandler.Card;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseDeviceStatus;

@Repository
public class KyDbPresenter {
    private final DeviceGroupRepository deviceGroupRepository;
    private final DbEmployeePresenter dbEmployeePresenter;
    private final DbSettingPresenter dbSettingPresenter;
    private final DbDevicePresenter dbDevicePresenter;
    private final DbRoutinePresenter dbRoutinePresenter;

    private int groupSize;

    private Logger logger = LoggerFactory.getLogger(KyDbPresenter.class);

    @Autowired
    public KyDbPresenter(DbRoutinePresenter dbRoutinePresenter, DbEmployeePresenter dbEmployeePresenter
            , DeviceGroupRepository deviceGroupRepository, DbDevicePresenter dbDevicePresenter
            , DbSettingPresenter dbSettingPresenter) {
        this.dbRoutinePresenter = dbRoutinePresenter;
        this.deviceGroupRepository = deviceGroupRepository;
        this.dbSettingPresenter = dbSettingPresenter;
        this.dbDevicePresenter = dbDevicePresenter;
        this.dbEmployeePresenter = dbEmployeePresenter;
    }

    /**
     * 调整(或增加)设备的数量，使之满足获取到的数据帧的要求
     *
     * @param groupId 获取到的数据帧的组 Id
     */
    private void initDbDeviceGroup(int groupId) {
        if (groupSize < groupId)
            groupSize = (int) deviceGroupRepository.count();

        while (groupSize < groupId) {
            groupSize++;
            deviceGroupRepository.save(new DeviceGroup(groupSize));
            dbDevicePresenter.InitDbDevices(groupSize);
        }
    }

    /**
     * 获得设备组的数量
     *
     * @return 设备组的数量
     */
    public int obtainDeviceGroupCount() {
        return (int) deviceGroupRepository.count();
    }

    /**
     * 处理心跳包，用户更新设备组和设备的时间
     *
     * @param tcpMsgHeartBeat 心跳包
     * @param autoCreate      自动在数据库中创建设备及设备组
     */
    private void handleMsgHeartBeat(IMessage tcpMsgHeartBeat, boolean autoCreate) {
        if (autoCreate)
            initDbDeviceGroup(tcpMsgHeartBeat.getGroupId());

        DeviceGroup deviceGroup = deviceGroupRepository.findByGroupId(tcpMsgHeartBeat.getGroupId());
        if (deviceGroup == null) {
            logger.warn("设备组(" + tcpMsgHeartBeat.getGroupId() + ") 不存在，心跳无法处理");
            return;
        }
        deviceGroup.setHeartBeatTime(new Date(System.currentTimeMillis()));
        deviceGroupRepository.save(deviceGroup);
        logger.info("设备组(" + tcpMsgHeartBeat.getGroupId() + ") 心跳处理完毕");
    }

    /**
     * 处理设备状态包
     *
     * @param tcpMsgResponseDeviceStatus 设备状态包
     * @param isDebug                    是否处于 Debug 模式
     */
    public void handleMsgDeviceStatus(TcpMsgResponseDeviceStatus tcpMsgResponseDeviceStatus, boolean isDebug) {
        long l1 = System.currentTimeMillis();

        //处理心跳，更新设备组信息，「Debug 模式下生成所需的设备和设备组」
        handleMsgHeartBeat(tcpMsgResponseDeviceStatus, isDebug);
        long l2 = System.currentTimeMillis();

        //更新设备的状态信息
        Device device = dbDevicePresenter.handleMsgDeviceStatus(tcpMsgResponseDeviceStatus);
        if (device == null) {
            logger.warn("设备(" + tcpMsgResponseDeviceStatus.getGroupId() + ", " + tcpMsgResponseDeviceStatus.getBoxId() + ") 不存在，无法处理");
            return;
        }
        long l3 = System.currentTimeMillis();

        //根据设备中记录的考勤表索引，获取考勤表
        if (device.getEmployeeObjectId() == null) {
            Employee employee = dbEmployeePresenter.createEmployee(device.getGroupId(), device.getBoxId());
            device.setEmployeeObjectId(employee.getId());
            dbDevicePresenter.updateDevice(device);
        }
        long l4 = System.currentTimeMillis();

        //更新员工的考勤表
        dbRoutinePresenter.updateRoutineById(device.getEmployeeObjectId(), tcpMsgResponseDeviceStatus);
        long l5 = System.currentTimeMillis();
        logger.info("时间耗费：" + (l2 - l1) + "ms; " + (l3 - l2) + "ms; " + (l4 - l3) + "ms; " + (l5 - l4) + "ms");
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
     * 设备初始化: 根据员工卡号获取员工信息
     *
     * @param cardNumber 员工卡号
     * @return 员工卡号所对应的员工信息
     */
    public Employee obtainDeviceByEmployeeCard(long cardNumber) {
        String employeeObjectId = dbDevicePresenter.obtainEmployeeObjectIdByCardNum(cardNumber);
        return dbEmployeePresenter.ObtainEmployeeByObjectId(employeeObjectId);
    }

    /**
     * 获取卡号的集合
     *
     * @return 卡号的集合
     */
    public long[] getCardArray(Card card) {

        return dbSettingPresenter.getCardArray(card);
    }

    /**
     * 将卡号保存到数据库
     *
     * @param freecards 卡号的数组
     * @param card      卡号类型
     * @return 是否保存成功
     */
    public boolean saveCardNumber(long[] freecards, Card card) {
        return dbSettingPresenter.saveCardArray(freecards, card);
    }
}
