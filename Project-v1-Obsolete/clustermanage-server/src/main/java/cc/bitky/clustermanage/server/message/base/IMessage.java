package cc.bitky.clustermanage.server.message.base;

public interface IMessage {
    int getMsgId();

    int getDeviceId();

    void setDeviceId(int deviceId);

    int getGroupId();

    void setGroupId(int groupId);
}
