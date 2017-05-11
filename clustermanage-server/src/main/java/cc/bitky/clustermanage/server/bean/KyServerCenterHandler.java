package cc.bitky.clustermanage.server.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cc.bitky.clustermanage.db.presenter.KyDbPresenter;
import cc.bitky.clustermanage.server.message.CardType;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.send.WebMsgSpecial;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployFreeCardNumber;

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
    boolean sendMsgToTcp(IMessage messages) {
        return serverTcpMessageHandler.sendMsgTrafficControl(messages);
    }

    /**
     * 从数据库中获取万能卡号并写入 Netty 的 Handler
     *
     * @param groupId  设备组 Id
     * @param deviceId 设备 Id
     * @return 万能卡号获取并写入 TCP 成功
     */
    boolean deployFreeCard(int groupId, int deviceId, int maxGroupId) {
        long[] freeCards = kyDbPresenter.getCardArray(CardType.FREE);
        IMessage CardMsg = new WebMsgDeployFreeCardNumber(groupId, deviceId, freeCards);
        return deployGroupedMessage(CardMsg, maxGroupId);
    }

    /**
     * 将收到的广播信息提取出来，并发送至 TCP 端
     *
     * @param message 原始信息
     * @return Message 信息是否已成功发送至 TCP 端
     */
    private boolean deployGroupedMessage(IMessage message, int maxGroupId) {
        boolean groupedGroup = message.getGroupId() == 255 || message.getGroupId() == 0;
        boolean groupedBox = message.getBoxId() == 255 || message.getBoxId() == 0;


        if (!groupedGroup && !groupedBox) {
            return sendMsgToTcp(message);
        }

        if (groupedGroup && groupedBox) {
            if (maxGroupId == 0)
                maxGroupId = kyDbPresenter.obtainDeviceGroupCount();
            if (maxGroupId == 0) return false;

            return sendMsgToTcp(WebMsgSpecial.forAll(message, maxGroupId));

//            for (int i = 1; i <= maxGroupId; i++) {
//                switch (message.getMsgId()) {
//                    case MsgType.SERVER_REMOTE_UNLOCK:
//                        WebMsgOperateBoxUnlock unlock = (WebMsgOperateBoxUnlock) message;
//                        if (!sendMsgToTcp(WebMsgGrouped.forBox(unlock.kyClone(i)))) return false;
//                        break;
//                    case MsgType.SERVER_SET_FREE_CARD_NUMBER:
//                        WebMsgDeployFreeCardNumber freeCardNumber = (WebMsgDeployFreeCardNumber) message;
//                        if (!sendMsgToTcp(WebMsgGrouped.forBox(freeCardNumber.kyClone(i)))) return false;
//                        break;
//                    case MsgType.INITIALIZE_SERVER_CLEAR_INITIALIZE_MESSAGE:
//                        WebMsgInitClearDeviceStatus clearDeviceStatus = (WebMsgInitClearDeviceStatus) message;
//                        if (!sendMsgToTcp(WebMsgGrouped.forBox(clearDeviceStatus.kyClone(i)))) return false;
//                        break;
//                    default:
//                        return false;
//                }
//            }
//            return true;
        }

        if (!groupedGroup && groupedBox) {
            return sendMsgToTcp(WebMsgSpecial.forBox(message));
        }

        return false;
    }

    /**
     * 服务器处理「 Web 信息 bean 」，更新设备的信息
     *
     * @param message Web信息 bean
     * @return 是否成功处理
     */
    boolean deployDeviceMsg(IMessage message, int maxgroupId) {
        return deployGroupedMessage(message, maxgroupId);
    }

    /**
     * 从数据库中获取卡号的集合
     *
     * @return 卡号的集合
     */
    long[] getCardArray(CardType card) {
        return kyDbPresenter.getCardArray(card);
    }

    /**
     * 将卡号保存到数据库
     *
     * @param freecards 卡号的数组
     * @param card      卡号类型
     * @return 是否保存成功
     */
    boolean saveCardNumber(long[] freecards, CardType card) {
        return kyDbPresenter.saveCardNumber(freecards, card);
    }


    /**
     * 检索数据库，给定的卡号是否匹配确认卡号
     *
     * @param cardNumber 待检索的卡号
     * @return 是否匹配确认卡号
     */
    boolean marchConfirmCard(long cardNumber) {
        return kyDbPresenter.marchConfirmCard(cardNumber);
    }
}
