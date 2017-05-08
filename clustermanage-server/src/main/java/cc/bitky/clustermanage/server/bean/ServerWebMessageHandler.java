package cc.bitky.clustermanage.server.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.presenter.KyDbPresenter;
import cc.bitky.clustermanage.server.message.CardType;
import cc.bitky.clustermanage.server.message.base.IMessage;

@Service
public class ServerWebMessageHandler {
    private final KyDbPresenter kyDbPresenter;
    private KyServerCenterHandler kyServerCenterHandler;
    private Logger logger = LoggerFactory.getLogger(ServerWebMessageHandler.class);

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
    public boolean deployFreeCard(int groupId, int deviceId) {
        return kyServerCenterHandler.deployFreeCard(groupId, deviceId);
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
     * @param messages Web信息 bean 的集合
     * @return 是否成功处理
     */
    public boolean deployDeviceMsg(List<IMessage> messages) {
        boolean isSuccess = true;
        for (IMessage message : messages) {
            if (!kyServerCenterHandler.deployDeviceMsg(message)) isSuccess = false;
        }
        return isSuccess;
    }

    /**
     * 服务器处理「 Web 信息 bean 」，更新设备的信息
     *
     * @param message Web信息 bean
     * @return 是否成功处理
     */
    public boolean deployDeviceMsg(IMessage message) {
        return kyServerCenterHandler.deployDeviceMsg(message);
    }

    void setKyServerCenterHandler(KyServerCenterHandler kyServerCenterHandler) {
        this.kyServerCenterHandler = kyServerCenterHandler;
    }


    /**
     * 从数据库中获取万能卡号的集合
     *
     * @return 万能卡号的集合
     */
    public long[] obtainFreeCards() {
        return kyServerCenterHandler.getCardArray(CardType.FREE);
    }

    /**
     * 从数据库中获取确认卡号的集合
     *
     * @return 确认卡号的集合
     */
    public long[] obtainConfirmCards() {
        return kyServerCenterHandler.getCardArray(CardType.CONFIRM);
    }

    /**
     * 将卡号保存到数据库
     *
     * @param freeCards 卡号的数组
     * @param card      卡号类型
     * @return 是否保存成功
     */
    public boolean saveCardNumber(long[] freeCards, CardType card) {
        return  kyServerCenterHandler.saveCardNumber(freeCards, card);
    }
}
