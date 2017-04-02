package cc.bitky.clustermanage.server.message;

public class BaseMessage implements IMessage {
  private int msgId = -1;
  private int groupId = -1;
  private int boxId = -1;

  BaseMessage(int groupId, int boxId) {
    this(groupId);
    this.boxId = boxId;
  }

  BaseMessage(int groupId) {
    this.groupId = groupId;
  }

  @Override public int getMsgId() {
    return msgId;
  }

  @Override public void setMsgId(int msgId) {
    this.msgId = msgId;
  }

  @Override public int getGroupId() {
    return groupId;
  }

  public int getBoxId() {
    return boxId;
  }
}
