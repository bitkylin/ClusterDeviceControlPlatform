package cc.bitky.clustermanage.server.message.send;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;
import cc.bitky.clustermanage.server.message.base.IMessage;

/**
 * 紧急地发送信息
 */
public class WebMsgSpecial extends BaseMessage {
    private final IMessage message;
    private boolean responsive = true;
    private boolean urgent = false;

    public WebMsgSpecial(IMessage message) {
        super(0, 0);
        this.message = message;
        setMsgId(MsgType.SERVER_SEND_SPECIAL);
    }

    public IMessage getMessage() {
        return message;
    }

    public boolean isResponsive() {
        return responsive;
    }

    public void setResponsive(boolean responsive) {
        this.responsive = responsive;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }
}
