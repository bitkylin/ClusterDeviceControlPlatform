package cc.bitky.clustermanage.server.message.base;


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
