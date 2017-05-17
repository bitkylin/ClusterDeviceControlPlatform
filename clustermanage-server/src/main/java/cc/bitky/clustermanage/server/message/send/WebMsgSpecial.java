package cc.bitky.clustermanage.server.message.send;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;
import cc.bitky.clustermanage.server.message.base.IMessage;

/**
 * 包裹需要使用特殊规则发送的信息
 */
public class WebMsgSpecial extends BaseMessage {

    private final IMessage message;
    private int maxGroupId;
    private int maxBoxId;
    private boolean responsive = true;
    private boolean urgent = false;
    private boolean grouped = false;

    public WebMsgSpecial(IMessage message) {
        super(0, 0);
        this.message = message;
        setMsgId(MsgType.SERVER_SEND_SPECIAL);
    }

    private WebMsgSpecial(int maxGroupId, IMessage message, boolean urgent, boolean responsive) {
        this(message);
        grouped = true;
        this.maxGroupId = maxGroupId;
        maxBoxId = 100;
        this.urgent = urgent;
        this.responsive = responsive;
    }

    public static WebMsgSpecial forAll(IMessage message, int maxGroupId, boolean urgent, boolean responsive) {
        return new WebMsgSpecial(maxGroupId, message, urgent, responsive);
    }

    public static WebMsgSpecial forBox(IMessage message, boolean urgent, boolean responsive) {
        return new WebMsgSpecial(0, message, urgent, responsive);
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

    public int getMaxGroupId() {
        return maxGroupId;
    }

    public int getMaxBoxId() {
        return maxBoxId;
    }

    public boolean isGrouped() {
        return grouped;
    }
}
