package cc.bitky.clustermanage.server.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.bitky.clustermanage.ServerSetting;
import cc.bitky.clustermanage.db.presenter.KyDbPresenter;
import cc.bitky.clustermanage.server.message.CardType;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.send.WebMsgSpecial;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployFreeCardNumber;
import cc.bitky.clustermanage.server.schedule.SendingMsgRepo;
import cc.bitky.clustermanage.web.bean.QueueInfo;

@Component
public class KyServerCenterHandler {
    private final ServerTcpMessageHandler serverTcpMessageHandler;
    private final KyDbPresenter kyDbPresenter;
    private final SendingMsgRepo sendingMsgRepo;

    @Autowired
    public KyServerCenterHandler(SendingMsgRepo sendingMsgRepo, ServerTcpMessageHandler serverTcpMessageHandler, ServerWebMessageHandler serverWebMessageHandler, KyDbPresenter kyDbPresenter) {
        this.sendingMsgRepo = sendingMsgRepo;
        this.serverTcpMessageHandler = serverTcpMessageHandler;
        serverTcpMessageHandler.setKyServerCenterHandler(this);
        serverWebMessageHandler.setKyServerCenterHandler(this);
        this.kyDbPresenter = kyDbPresenter;
    }

    public SendingMsgRepo getSendingMsgRepo() {
        return sendingMsgRepo;
    }

    /**
     * Server 模块将「 Web 信息 bean 」写入 Netty 的 Handler
     *
     * @param messages Web信息 bean 的集合
     * @return 是否成功写入 Netty 处理通道
     */
    boolean sendMsgTrafficControl(IMessage messages) {
        return serverTcpMessageHandler.sendMsgTrafficControl(messages);
    }

    /**
     * Server 模块将「 Web 信息 bean 」写入 Netty 的 Handler
     *
     * @param messages Web信息 bean 的集合
     * @return 是否成功写入 Netty 处理通道
     */
    private boolean sendMsgToTcpSpecial(IMessage messages, boolean urgent, boolean responsive) {
        return serverTcpMessageHandler.sendMsgToTcpSpecial(messages, urgent, responsive);
    }

    /**
     * 从数据库中获取万能卡号并写入 Netty 的 Handler
     *
     * @param groupId  设备组 Id
     * @param deviceId 设备 Id
     * @return 万能卡号获取并写入 TCP 成功
     */
    boolean deployFreeCard(int groupId, int deviceId, int maxGroupId) {
        String[] freeCards = kyDbPresenter.getCardArray(CardType.FREE);
        IMessage CardMsg = new WebMsgDeployFreeCardNumber(groupId, deviceId, freeCards);
        return deployGroupedMessage(CardMsg, maxGroupId, false, true);
    }

    /**
     * 将收到的广播信息提取出来，并发送至 TCP 端
     *
     * @param message 原始信息
     * @return Message 信息是否已成功发送至 TCP 端
     */
    private boolean deployGroupedMessage(IMessage message, int maxGroupId, boolean urgent, boolean responsive) {
        boolean groupedGroup = message.getGroupId() == 255 || message.getGroupId() == 0;
        boolean groupedBox = message.getDeviceId() == 255 || message.getDeviceId() == 0;


        if (!groupedGroup && !groupedBox) {
            return sendMsgToTcpSpecial(message, urgent, responsive);
        }

        if (groupedGroup && groupedBox) {
            if (maxGroupId == 0)
                maxGroupId = kyDbPresenter.obtainDeviceGroupCount();
            if (maxGroupId == 0) return false;

            return sendMsgTrafficControl(WebMsgSpecial.forAll(message, maxGroupId, urgent, responsive));
        }

        if (!groupedGroup && groupedBox) {
            return sendMsgTrafficControl(WebMsgSpecial.forBox(message, urgent, responsive));
        }

        return false;
    }

    /**
     * 服务器处理「 Web 信息 bean 」，更新设备的信息
     *
     * @param message Web信息 bean
     * @return 是否成功处理
     */
    boolean deployDeviceMsg(IMessage message, int maxGroupId, boolean urgent, boolean responsive) {
        return deployGroupedMessage(message, maxGroupId, urgent, responsive);
    }

    /**
     * 从数据库中获取卡号的集合
     *
     * @return 卡号的集合
     */
    String[] getCardArray(CardType card) {
        return kyDbPresenter.getCardArray(card);
    }

    /**
     * 将卡号保存到数据库
     *
     * @param freeCards 卡号的数组
     * @param card      卡号类型
     * @return 是否保存成功
     */
    boolean saveCardNumber(String[] freeCards, CardType card) {
        return kyDbPresenter.saveCardNumber(freeCards, card);
    }


    /**
     * 检索数据库，给定的卡号是否匹配确认卡号
     *
     * @param cardNumber 待检索的卡号
     * @return 是否匹配确认卡号
     */
    boolean marchConfirmCard(String cardNumber) {
        return kyDbPresenter.marchConfirmCard(cardNumber);
    }

    QueueInfo obtainQueueFrame() {
        int size = getSendingMsgRepo().getLinkedBlockingDeque().size();
        int capacity = ServerSetting.LINKED_DEQUE_LIMIT_CAPACITY;
        int interval = ServerSetting.FRAME_SEND_INTERVAL;
        return new QueueInfo(size, capacity, interval);
     }
}
