package cc.bitky.clustermanage.netty.message.base;

public interface IMessage {
    int getMsgId();

    void setMsgId(int msgId);

    int getBoxId();

    void setBoxId(int boxId);

    int getGroupId();

    void setGroupId(int groupId);
}
