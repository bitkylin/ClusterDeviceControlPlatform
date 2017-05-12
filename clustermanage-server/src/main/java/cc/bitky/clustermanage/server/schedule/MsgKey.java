package cc.bitky.clustermanage.server.schedule;

public class MsgKey {
    private byte msgId;
    private byte groupId;
    private byte boxId;

    public MsgKey(byte groupId, byte boxId, byte msgId) {
        this.groupId = groupId;
        this.boxId = boxId;
        this.msgId = msgId;
    }


    public void increaseMsgId(int n) {
        msgId += n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MsgKey msgKey = (MsgKey) o;

        return msgId == msgKey.msgId && groupId == msgKey.groupId && boxId == msgKey.boxId;
    }

    @Override
    public int hashCode() {
        int result = msgId & 0xff;
        result = 31 * result + (groupId & 0xff);
        result = 31 * result + (boxId & 0xff);
        return result;
    }

    public byte getMsgId() {
        return msgId;
    }

    public byte getGroupId() {
        return groupId;
    }

    public byte getBoxId() {
        return boxId;
    }

    @Override
    public String toString() {
        return "MsgKey{" +
                "msgId=" + msgId +
                ", groupId=" + groupId +
                ", boxId=" + boxId +
                '}';
    }
}
