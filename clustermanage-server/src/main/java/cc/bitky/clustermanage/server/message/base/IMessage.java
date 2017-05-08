package cc.bitky.clustermanage.server.message.base;

public interface IMessage {
    int getMsgId();

    int getBoxId();

    void setBoxId(int boxId);

    int getGroupId();

    void setGroupId(int groupId);

}
