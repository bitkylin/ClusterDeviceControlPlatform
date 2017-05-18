package cc.bitky.clustermanage.netty.message.base;


public class BaseTcpResponseMsg extends BaseMessage {

    private final int status;

    public BaseTcpResponseMsg(int groupId, int boxId, int status) {
        super(groupId, boxId);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
