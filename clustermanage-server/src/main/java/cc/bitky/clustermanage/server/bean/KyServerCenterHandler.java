package cc.bitky.clustermanage.server.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import cc.bitky.clustermanage.db.presenter.KyDbPresenter;
import cc.bitky.clustermanage.server.bean.ServerWebMessageHandler.Card;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployFreeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgGrouped;

@Component
public class KyServerCenterHandler {
    private final ServerTcpMessageHandler serverTcpMessageHandler;
    private final KyDbPresenter kyDbPresenter;

    @Autowired
    public KyServerCenterHandler(ServerTcpMessageHandler serverTcpMessageHandler, ServerWebMessageHandler serverWebMessageHandler, KyDbPresenter kyDbPresenter) {
        this.serverTcpMessageHandler = serverTcpMessageHandler;
        serverTcpMessageHandler.setKyServerCenterHandler(this);
        serverWebMessageHandler.setKyServerCenterHandler(this);
        this.kyDbPresenter = kyDbPresenter;
    }

    /**
     * Server 模块将「 Web 信息 bean 」写入 Netty 的 Handler
     *
     * @param messages Web信息 bean 的集合
     * @return 是否成功写入 Netty 处理通道
     */
    private boolean sendMsgToTcp(List<IMessage> messages) {
        return serverTcpMessageHandler.sendMsgToTcp(messages);
    }

    /**
     * 从数据库中获取万能卡号并写入 Netty 的 Handler
     *
     * @param groupId  设备组 Id
     * @param deviceId 设备 Id
     * @return 万能卡号获取并写入 TCP 成功
     */
    boolean deployFreeCard(int groupId, int deviceId) {
        long[] freeCards = kyDbPresenter.getCardArray(Card.FREE);
        IMessage CardMsg = new WebMsgDeployFreeCardNumber(groupId, deviceId, freeCards);
        return deployGroupedMessage(CardMsg);
    }

    /**
     * 将收到的广播信息提取出来，并发送至 TCP 端
     *
     * @param message 原始信息
     * @return Message 信息是否已成功发送至 TCP 端
     */
    private boolean deployGroupedMessage(IMessage message) {
        boolean groupedGroup = message.getGroupId() == 255 || message.getGroupId() == 0;
        boolean groupedBox = message.getBoxId() == 255 || message.getBoxId() == 0;
        List<IMessage> messages = new ArrayList<>(1);

        if (groupedGroup && groupedBox) {
            int groupCount = kyDbPresenter.obtainDeviceGroupCount();
            WebMsgGrouped Groupedmsg = WebMsgGrouped.forAll(groupCount, message);
            messages.add(Groupedmsg);
        }

        if (!groupedGroup && groupedBox) {
            WebMsgGrouped Groupedmsg = WebMsgGrouped.forBox(message);
            messages.add(Groupedmsg);
        }

        if (!(groupedGroup || groupedBox)) {
            messages.add(message);
        }
        return messages.size() > 0 && sendMsgToTcp(messages);
    }

    /**
     * 服务器处理「 Web 信息 bean 」，更新设备的信息
     *
     * @param message Web信息 bean
     * @return 是否成功处理
     */
    boolean deployDeviceMsg(IMessage message) {
        return deployGroupedMessage(message);
    }

    /**
     * 从数据库中获取卡号的集合
     *
     * @return 卡号的集合
     */
    long[] getCardArray(Card card) {
        return kyDbPresenter.getCardArray(card);
    }

    /**
     * 将卡号保存到数据库
     *
     * @param freecards 卡号的数组
     * @param card      卡号类型
     * @return 是否保存成功
     */
    boolean saveCardNumber(long[] freecards, Card card) {
        return kyDbPresenter.saveCardNumber(freecards, card);
    }
}
