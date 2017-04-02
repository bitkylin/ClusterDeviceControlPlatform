package cc.bitky.clustermanage.server.message;

public interface IMessage {
  int getMsgId();

  void setMsgId(int msgId);

  int getGroupId();
}
