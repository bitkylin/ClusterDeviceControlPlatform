package cc.bitky.clustermanage.server.message.send;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;
import cc.bitky.clustermanage.server.message.base.IMessage;

/**
 * 包裹需要使用特殊规则发送的信息
 */
public class WebMsgSpecial extends BaseMessage {

    private final IMessage message;
    private Tpye tpye = Tpye.NONE;
    private int maxGroupId;
    private int maxDeviceId;
    private boolean responsive = true;
    private boolean urgent = false;

    public WebMsgSpecial(IMessage message) {
        super(0, 0);
        this.message = message;
        setMsgId(MsgType.SERVER_SEND_SPECIAL);
    }

    private WebMsgSpecial(int maxGroupId, int maxDeviceId, IMessage message, boolean urgent, boolean responsive, Tpye tpye) {
        this(message);
        this.tpye = tpye;
        this.maxGroupId = maxGroupId;
        this.maxDeviceId = maxDeviceId;
        this.urgent = urgent;
        this.responsive = responsive;
    }

    public static WebMsgSpecial forAll(IMessage message, int maxGroupId, boolean urgent, boolean responsive) {
        return new WebMsgSpecial(maxGroupId, 100, message, urgent, responsive, Tpye.ALL);
    }

    public static WebMsgSpecial forBox(IMessage message, boolean urgent, boolean responsive) {
        return new WebMsgSpecial(0, 100, message, urgent, responsive, Tpye.BOX);
    }

    public static WebMsgSpecial forGroup(IMessage message, int maxGroupId, boolean urgent, boolean responsive) {
        return new WebMsgSpecial(maxGroupId, 120, message, urgent, responsive, Tpye.GROUP);
    }

    public Tpye getTpye() {
        return tpye;
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

    public int getMaxDeviceId() {
        return maxDeviceId;
    }

    public enum Tpye {
        GROUP,
        BOX,
        ALL,
        NONE
    }
}
