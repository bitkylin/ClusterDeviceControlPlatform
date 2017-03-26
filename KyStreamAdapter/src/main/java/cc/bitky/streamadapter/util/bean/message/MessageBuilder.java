package cc.bitky.streamadapter.util.bean.message;

public class MessageBuilder {
  private static MessageBuilder messageBuilder = null;
  private int MsgId = -1;
  private String rawMsg;

  private MessageBuilder() {
  }

  public static MessageBuilder newInstance() {
    if (messageBuilder == null) {
      messageBuilder = new MessageBuilder();
    }
    return messageBuilder;
  }

  public IMessage build() {
    IMessage message;
    message = objBuildMessage(MsgId, rawMsg);

    MsgId = -1;
    rawMsg = null;

    return message;
  }

  private IMessage objBuildMessage(int msgId, String rawMsg) {

    return null;
  }

  public MessageBuilder MessageId(int MsgId) {
    this.MsgId = MsgId;
    return this;
  }

  MessageBuilder rawMsg(String rawMsg) {
    this.rawMsg = rawMsg;
    return this;
  }
}
