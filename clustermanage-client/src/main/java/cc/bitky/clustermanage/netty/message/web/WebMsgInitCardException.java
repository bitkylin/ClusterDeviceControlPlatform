package cc.bitky.clustermanage.netty.message.web;


import cc.bitky.clustermanage.netty.message.CardType;
import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

/**
 * 设备初始化「Exception」: 服务器匹配接收到的卡号异常
 */
public class WebMsgInitCardException extends BaseMessage {

    private CardType cardType;

    public WebMsgInitCardException(int groupId, int boxId, CardType cardType) {
        super(groupId, boxId);
        this.cardType = cardType;
        setMsgId(MsgType.INITIALIZE_SERVER_MARCH_CARD_EXCEPTION);
    }

    public CardType getCardType() {
        return cardType;
    }
}
