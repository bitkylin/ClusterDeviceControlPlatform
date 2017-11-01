package cc.bitky.clustermanage.netty.message.base;

public class BaseMsgCardNum extends BaseMessage {

    private long cardNumber;

    protected BaseMsgCardNum(int groupId, int boxId, long cardNumber, int msgType) {
        super(groupId, boxId);
        setMsgId(msgType);
        this.cardNumber = cardNumber;
    }

    public long getCardNumber() {
        return cardNumber;
    }
}
