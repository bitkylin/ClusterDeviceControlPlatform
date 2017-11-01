package cc.bitky.clustermanage.server.message.base;

/**
 * 基础信息 Message
 */
public class BaseMessage implements IMessage {

    private int msgId = -1;
    private int groupId = -1;
    private int deviceId = -1;

    protected BaseMessage(int groupId) {
        this.groupId = groupId;
    }

    protected BaseMessage(int groupId, int deviceId) {
        this(groupId);
        this.deviceId = deviceId;
    }

    @Override
    public int getMsgId() {
        return msgId;
    }

    protected void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    @Override
    public int getGroupId() {
        return groupId;
    }

    @Override
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public int getDeviceId() {
        return deviceId;
    }

    @Override
    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }
}
