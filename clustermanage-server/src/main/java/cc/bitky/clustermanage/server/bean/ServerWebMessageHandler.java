package cc.bitky.clustermanage.server.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.bean.Employee;
import cc.bitky.clustermanage.db.presenter.KyDbPresenter;
import cc.bitky.clustermanage.global.CommSetting;
import cc.bitky.clustermanage.global.DbSetting;
import cc.bitky.clustermanage.server.message.CardType;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployRemainChargeTimes;
import cc.bitky.clustermanage.web.bean.QueueDevice;
import cc.bitky.clustermanage.web.bean.QueueInfo;

@Service
public class ServerWebMessageHandler {
    private final KyDbPresenter kyDbPresenter;
    private KyServerCenterHandler kyServerCenterHandler;

    @Autowired
    public ServerWebMessageHandler(KyDbPresenter kyDbPresenter) {
        this.kyDbPresenter = kyDbPresenter;
    }

    /**
     * 从数据库中获取万能卡号，并写入 Netty 的 Handler
     *
     * @param groupId  设备组 Id
     * @param deviceId 设备 Id
     * @return 万能卡号获取并写入 TCP 成功
     */
    public boolean deployFreeCard(int groupId, int deviceId, int maxgroupId) {
        return kyServerCenterHandler.deployFreeCard(groupId, deviceId, maxgroupId);
    }

    /**
     * 从数据库中获取设备的信息
     *
     * @param groupId  设备组 Id
     * @param deviceId 设备 Id
     * @return 设备信息的集合
     */
    public List<Device> getDeviceInfo(int groupId, int deviceId) {
        return kyDbPresenter.getDevices(groupId, deviceId);
    }

    /**
     * 服务器处理「 Web 信息 bean 」，更新设备的信息
     *
     * @param message Web信息 bean
     * @return 是否成功处理
     */
    public boolean deployDeviceMsg(IMessage message, int maxgroupId, boolean urgent, boolean responsive) {
        return kyServerCenterHandler.deployDeviceMsg(message, maxgroupId, urgent, responsive);
    }

    void setKyServerCenterHandler(KyServerCenterHandler kyServerCenterHandler) {
        this.kyServerCenterHandler = kyServerCenterHandler;
    }

    /**
     * 从数据库中获取万能卡号的集合
     *
     * @return 万能卡号的集合
     */
    public String[] obtainFreeCards() {
        return kyServerCenterHandler.getCardArray(CardType.FREE);
    }

    /**
     * 从数据库中获取确认卡号的集合
     *
     * @return 确认卡号的集合
     */
    public String[] obtainConfirmCards() {
        return kyServerCenterHandler.getCardArray(CardType.CONFIRM);
    }

    /**
     * 将卡号保存到数据库
     *
     * @param freeCards 卡号的数组
     * @param card      卡号类型
     * @return 是否保存成功
     */
    public boolean saveCardNumber(String[] freeCards, CardType card) {
        return kyServerCenterHandler.saveCardNumber(freeCards, card);
    }

    /**
     * 从数据库中获取并更新设备的信息
     *
     * @param groupId     设备组 ID
     * @param deviceId    设备 ID
     * @param queueDevice 需要部署的设备信息
     * @param maxGroupId  若更新多个设备组，可指定更新设备组的 ID 范围为: 1 - maxGroupId
     * @return 更新是否成功
     */
    public boolean obtainDeployDeviceMsg(int groupId, int deviceId, QueueDevice queueDevice, int maxGroupId) {
        if (groupId == 255 || groupId == 0) {
            if (maxGroupId == 0)
                maxGroupId = kyDbPresenter.obtainMaxDeviceGroupId();
            if (maxGroupId == 0) return false;
            for (int i = 1; i <= maxGroupId; i++) {
                getDeviceInfo(i, deviceId).forEach(device -> deployEmployeeMsg(queueDevice, device));
            }

        } else getDeviceInfo(groupId, deviceId)
                .forEach(device -> deployEmployeeMsg(queueDevice, device));
        return true;
    }

    /**
     * 部署员工的姓名，单位，卡号
     *
     * @param device      员工对应的设备
     * @param queueDevice 需要部署的设备信息
     */
    private void deployEmployeeMsg(QueueDevice queueDevice, Device device) {
        boolean name = queueDevice.isPostName();
        boolean department = queueDevice.isPostDepartment();
        boolean cardNumber = queueDevice.isPostCardNumber();
        boolean remainChargeTime = queueDevice.isPostRemainChargeTime();

        boolean AUTO_INIT = CommSetting.DEPLOY_DEVICES_INIT;

        if (device == null) return;

        //部署卡号
        if (cardNumber && device.getCardNumber() != null)
            kyServerCenterHandler.sendMsgTrafficControl(new WebMsgDeployEmployeeCardNumber(device.getGroupId(), device.getDeviceId(), device.getCardNumber()));
        else if (cardNumber && AUTO_INIT)
            kyServerCenterHandler.sendMsgTrafficControl(new WebMsgDeployEmployeeCardNumber(device.getGroupId(), device.getDeviceId(), DbSetting.DEFAULT_EMPLOYEE_CARD_NUMBER));

        //部署剩余充电次数
        if (remainChargeTime) {
            int remainTimes = device.getRemainChargeTime();
            remainTimes = remainTimes > 0 ? remainTimes : 0;
            remainTimes = remainTimes <= 100 ? remainTimes : 100;
            kyServerCenterHandler.sendMsgTrafficControl(new WebMsgDeployRemainChargeTimes(device.getGroupId(), device.getDeviceId(), remainTimes));
        }

        //部署姓名和单位
        if (!(name || department)) return;
        Employee employee = kyDbPresenter.obtainEmployeeByEmployeeObjectId(device.getEmployeeObjectId());

        if (employee != null) {
            if (name && employee.getName() != null)
                kyServerCenterHandler.sendMsgTrafficControl(new WebMsgDeployEmployeeName(device.getGroupId(), device.getDeviceId(), employee.getName()));
            else if (name && AUTO_INIT)
                kyServerCenterHandler.sendMsgTrafficControl(new WebMsgDeployEmployeeName(device.getGroupId(), device.getDeviceId(), DbSetting.DEFAULT_EMPLOYEE_NAME));

            if (department && employee.getDepartment() != null)
                kyServerCenterHandler.sendMsgTrafficControl(new WebMsgDeployEmployeeDepartment(device.getGroupId(), device.getDeviceId(), employee.getDepartment()));
            else if (department && AUTO_INIT)
                kyServerCenterHandler.sendMsgTrafficControl(new WebMsgDeployEmployeeDepartment(device.getGroupId(), device.getDeviceId(), DbSetting.DEFAULT_EMPLOYEE_DEPARTMENT));

        } else if (AUTO_INIT) {
            if (name)
                kyServerCenterHandler.sendMsgTrafficControl(new WebMsgDeployEmployeeName(device.getGroupId(), device.getDeviceId(), DbSetting.DEFAULT_EMPLOYEE_NAME));
            if (department)
                kyServerCenterHandler.sendMsgTrafficControl(new WebMsgDeployEmployeeDepartment(device.getGroupId(), device.getDeviceId(), DbSetting.DEFAULT_EMPLOYEE_DEPARTMENT));
        }
    }

    public QueueInfo obtainQueueFrame() {
        return kyServerCenterHandler.obtainQueueFrame();
    }

    /**
     * 「操作」更换新的设备，将设备的剩余充电次数恢复为最大值
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return 操作成功
     */
    public boolean devicesRenew(int groupId, int deviceId) {
        return kyDbPresenter.devicesRenew(groupId, deviceId);
    }

    /**
     * 从数据库中获取正在活动的设备组
     *
     * @return 设备集合
     */
    public List<Integer> getDeviceGroupActivated() {
        return kyDbPresenter.getDeviceGroupActivated();
    }
}
