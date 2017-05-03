package cc.bitky.clustermanage.netty.message.web;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;
import cc.bitky.clustermanage.netty.message.base.IMessage;

/**
 * 成组地发送消息
 */
public class WebMsgGrouped extends BaseMessage {
    private final IMessage message;
    private final int maxGroupId;
    private final int maxBoxId;
    private GroupedEnum groupedEnum;

    private WebMsgGrouped(int maxGroupId, IMessage message, GroupedEnum groupedEnum) {
        super(0, 0);
        this.maxGroupId = maxGroupId;
        this.message = message;
        this.groupedEnum = groupedEnum;
        maxBoxId = 100;
        setMsgId(MsgType.SERVER_SEND_GROUPED);
    }

    public static WebMsgGrouped forBox(IMessage message) {
        return new WebMsgGrouped(0, message, GroupedEnum.BOX);
    }

    public static WebMsgGrouped forAll(int maxGroupId, IMessage message) {
        return new WebMsgGrouped(maxGroupId, message, GroupedEnum.ALL);
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

    public GroupedEnum getGroupedEnum() {
        return groupedEnum;
    }

    public enum GroupedEnum {
        GROUP,
        BOX,
        ALL
    }
}
