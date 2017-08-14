package cc.bitky.clustermanage.server.message.base;


public class BaseTcpResponseMsg extends BaseMessage {

    private final int status;

    public BaseTcpResponseMsg(int groupId, int deviceId, int status) {
        super(groupId, deviceId);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
