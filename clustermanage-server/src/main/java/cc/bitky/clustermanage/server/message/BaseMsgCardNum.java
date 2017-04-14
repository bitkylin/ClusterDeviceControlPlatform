package cc.bitky.clustermanage.server.message;

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
