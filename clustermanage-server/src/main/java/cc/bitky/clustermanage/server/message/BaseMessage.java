package cc.bitky.clustermanage.server.message;

public class BaseMessage implements IMessage {
    private int msgId = -1;
    private int groupId = -1;
    private int boxId = -1;

    protected BaseMessage(int groupId) {
        this.groupId = groupId;
    }

    protected BaseMessage(int groupId, int boxId) {
        this(groupId);
        this.boxId = boxId;
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
    public int getBoxId() {
        return boxId;
    }

    @Override
    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }
}
