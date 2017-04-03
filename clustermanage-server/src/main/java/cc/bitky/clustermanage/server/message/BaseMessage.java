package cc.bitky.clustermanage.server.message;

public class BaseMessage implements IMessage {
  private int msgId = -1;
  private int groupId = -1;
  private int boxId = -1;

  BaseMessage(int groupId) {
    this.groupId = groupId;
  }

  BaseMessage(int groupId, int boxId) {
    this(groupId);
    this.boxId = boxId;
  }

  @Override public int getMsgId() {
    return msgId;
  }

  void setMsgId(int msgId) {
    this.msgId = msgId;
  }

  @Override public int getGroupId() {
    return groupId;
  }

  public int getBoxId() {
    return boxId;
  }
}
