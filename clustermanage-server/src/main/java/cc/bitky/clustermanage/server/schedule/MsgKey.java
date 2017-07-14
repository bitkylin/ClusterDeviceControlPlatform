package cc.bitky.clustermanage.server.schedule;

public class MsgKey {
    private byte msgId;
    private byte groupId;
    private byte deviceId;

    public MsgKey(byte groupId, byte deviceId, byte msgId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
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

        return msgId == msgKey.msgId && groupId == msgKey.groupId && deviceId == msgKey.deviceId;
    }

    @Override
    public int hashCode() {
        int result = msgId & 0xff;
        result = 31 * result + (groupId & 0xff);
        result = 31 * result + (deviceId & 0xff);
        return result;
    }

    public byte getMsgId() {
        return msgId;
    }

    public byte getGroupId() {
        return groupId;
    }

    public byte getDeviceId() {
        return deviceId;
    }

    @Override
    public String toString() {
        return "MsgKey{" +
                "msgId=" + msgId +
                ", groupId=" + groupId +
                ", deviceId=" + deviceId +
                '}';
    }
}
