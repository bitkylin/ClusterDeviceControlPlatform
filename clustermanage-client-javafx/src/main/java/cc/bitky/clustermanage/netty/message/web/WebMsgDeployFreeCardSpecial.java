package cc.bitky.clustermanage.netty.message.web;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

/**
 * 服务器部署万能卡号
 */
public class WebMsgDeployFreeCardSpecial extends BaseMessage {

    private final int itemId;
    private long cardNumber;

    /**
     * 服务器部署万能卡号
     *
     * @param groupId    组号
     * @param boxId      设备号
     * @param cardNumber 万能卡号集合
     */
    public WebMsgDeployFreeCardSpecial(int groupId, int boxId, long cardNumber, int itemId) {
        super(groupId, boxId);
        this.cardNumber = cardNumber;
        this.itemId = itemId;
        setMsgId(MsgType.SERVER_SET_FREE_CARD_NUMBER);
    }

    public int getItemId() {
        return itemId;
    }

    public long getCardNumber() {
        return cardNumber;
    }
}
