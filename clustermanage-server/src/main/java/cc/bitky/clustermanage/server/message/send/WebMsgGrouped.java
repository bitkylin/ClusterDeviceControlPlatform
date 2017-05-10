package cc.bitky.clustermanage.server.message.send;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;
import cc.bitky.clustermanage.server.message.base.IMessage;

/**
 * 成组地发送消息
 */
public class WebMsgGrouped extends BaseMessage {
    private final int maxGroupId;
    private final int maxBoxId;
    private final IMessage message;

    private WebMsgGrouped(int maxGroupId, IMessage message) {
        super(0, 0);
        this.maxGroupId = maxGroupId;
        maxBoxId = 100;
        this.message = message;
        setMsgId(MsgType.SERVER_SEND_GROUPED);
    }

    public static WebMsgGrouped forBox(IMessage message) {
        return new WebMsgGrouped(0, message);
    }

    public IMessage getMessage() {
        return message;
    }

    public int getMaxGroupId() {
        return maxGroupId;
    }

    public int getMaxBoxId() {
        return maxBoxId;
    }
}
