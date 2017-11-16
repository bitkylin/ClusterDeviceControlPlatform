package cc.bitky.clusterdeviceplatform.server.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cc.bitky.clusterdeviceplatform.messageutils.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecDeviceClear;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecDeviceRemainChargeTimes;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecDeviceUnlock;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecEmployeeDepartment;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecEmployeeName;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardClear;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardConfirm;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardEmployee;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardFree;
import cc.bitky.clusterdeviceplatform.server.config.CommSetting;
import cc.bitky.clusterdeviceplatform.server.config.WebSetting;
import cc.bitky.clusterdeviceplatform.server.db.DbPresenter;
import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.db.bean.Employee;
import cc.bitky.clusterdeviceplatform.server.web.bean.CardType;
import cc.bitky.clusterdeviceplatform.server.web.bean.QueueDevice;
import reactor.core.publisher.Mono;

@Service
public class ServerWebProcessor {

    private final ServerCenterProcessor centerProcessor;
    private final DbPresenter dbPresenter;

    {

    }

    @Autowired
    public ServerWebProcessor(ServerCenterProcessor centerProcessor, DbPresenter dbPresenter) {
        this.centerProcessor = centerProcessor;
        this.dbPresenter = dbPresenter;
    }

    /**
     * 根据类型获取指定的卡号组
     *
     * @param type 卡号组的类型
     * @return 特定的卡号组
     */
    public Mono<String[]> queryCards(CardType type) {
        return dbPresenter.queryCardSet(type);
    }

    /**
     * 保存特定的卡号组
     *
     * @param cards 特定的卡号组
     * @param type  卡号组的类型
     * @return 保存成功
     */
    public boolean saveCards(String[] cards, CardType type) {
        return dbPresenter.saveCardSet(cards, type);
    }

    /**
     * 向设备部署卡号的集合
     *
     * @param groupId  设备组号
     * @param deviceId 设备号
     * @param type     卡号类型
     * @return 部署成功
     */
    public boolean ungroupCardSet(int groupId, int deviceId, CardType type) {
        Mono<String[]> mono = queryCards(type);
        String[] cards = mono.block();
        List<Integer> cardInt = new ArrayList<>(cards.length);
        for (String card : cards) {
            cardInt.add(Integer.parseUnsignedInt(card, 16));
        }
        if (groupId == WebSetting.BROADCAST_GROUP_ID && deviceId == WebSetting.BROADCAST_DEVICE_ID) {
            for (int tempGroupId = 1; tempGroupId <= DeviceSetting.MAX_GROUP_ID; tempGroupId++) {
                for (int tempDeviceId = 1; tempDeviceId <= DeviceSetting.MAX_DEVICE_ID; tempDeviceId++) {
                    deployCards(tempGroupId, tempDeviceId, cardInt, type);
                }
            }
        }
        if (groupId == WebSetting.BROADCAST_GROUP_ID) {
            for (int tempGroupId = 1; tempGroupId <= DeviceSetting.MAX_GROUP_ID; tempGroupId++) {
                deployCards(tempGroupId, deviceId, cardInt, type);
            }
        }
        if (deviceId == WebSetting.BROADCAST_DEVICE_ID) {
            for (int tempDeviceId = 1; tempDeviceId <= DeviceSetting.MAX_DEVICE_ID; tempDeviceId++) {
                deployCards(groupId, tempDeviceId, cardInt, type);
            }
        }
        return true;
    }

    private boolean deployCards(int groupId, int deviceId, List<Integer> cards, CardType type) {
        switch (type) {
            case Free:
                return centerProcessor.sendMessage(MsgCodecCardFree.create(groupId, deviceId, cards));
            case Confirm:
                return centerProcessor.sendMessage(MsgCodecCardConfirm.create(groupId, deviceId, cards));
            case Clear:
                return centerProcessor.sendMessage(MsgCodecCardClear.create(groupId, deviceId, cards));
            default:
                return false;
        }
    }

    /**
     * 从数据库中检索特定的员工信息
     *
     * @param objectId 员工 ObjectId
     * @return 已检索出的特定员工信息
     */
    private Optional<Employee> queryEmployee(String objectId) {
        return dbPresenter.queryEmployee(objectId);
    }

    /**
     * 根据特定条件，检索并向设备部署相应的消息对象
     *
     * @param queue 特定的部署条件
     * @return 部署成功
     */
    public boolean queryAndDeployDeviceMsg(QueueDevice queue) {
        if (queue.getGroupId() == WebSetting.BROADCAST_GROUP_ID) {
            for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
                dbPresenter.queryDeviceInfo(i, queue.getDeviceId()).forEach(device -> deployEmployeeMsg(device, queue));
            }
        } else {
            dbPresenter.queryDeviceInfo(queue.getGroupId(), queue.getDeviceId()).forEach(device -> deployEmployeeMsg(device, queue));
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
            Optional<Employee> optional = queryEmployee(device.getId());
            if (optional.isPresent() && queue.isName()) {
                centerProcessor.sendMessage(MsgCodecEmployeeName.create(device.getGroupId(), device.getDeviceId(), optional.get().getName()));
            }
            if (optional.isPresent() && queue.isDepartment()) {
                centerProcessor.sendMessage(MsgCodecEmployeeDepartment.create(device.getGroupId(), device.getDeviceId(), optional.get().getDepartment()));
            }
        }
        if (queue.isCard()) {
            centerProcessor.sendMessage(MsgCodecCardEmployee.create(device.getGroupId(), device.getDeviceId(), device.getCardNumber()));
        }
        if (queue.isRemainChargeTime()) {
            centerProcessor.sendMessage(MsgCodecDeviceRemainChargeTimes.create(device.getGroupId(), device.getDeviceId(), device.getRemainChargeTime()));
        }
        if (queue.isRenew()) {
            device.setRemainChargeTime(CommSetting.DEVICE_INIT_CHARGE_TIMES);
            dbPresenter.saveDeviceInfo(device);
        }
        if (queue.isUnLock()) {
            centerProcessor.sendMessage(MsgCodecDeviceUnlock.create(device.getGroupId(), device.getDeviceId()));
        }
        if (queue.isInitialize()) {
            centerProcessor.sendMessage(MsgCodecDeviceClear.create(device.getGroupId(), device.getDeviceId()));
        }
    }
}
