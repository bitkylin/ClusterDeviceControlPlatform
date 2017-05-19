package cc.bitky.clustermanage.netty.message.base;

public class BaseMsgCardNum extends BaseMessage {

    private String cardNumber;

    protected BaseMsgCardNum(int groupId, int boxId, String cardNumber, int msgType) {
        super(groupId, boxId);
        setMsgId(msgType);
        this.cardNumber = cardNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }
}
