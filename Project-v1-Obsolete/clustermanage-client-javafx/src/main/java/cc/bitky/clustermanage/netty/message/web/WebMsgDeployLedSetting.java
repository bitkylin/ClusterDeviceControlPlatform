package cc.bitky.clustermanage.netty.message.web;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

/**
 * 服务器部署员工卡号
 */
public class WebMsgDeployLedSetting extends BaseMessage {
    String text;

    public WebMsgDeployLedSetting(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.SERVER_LED_SETTING);
    }

    public WebMsgDeployLedSetting(int groupId, int boxId, String text) {
        this(groupId, boxId);
        this.text = text;
    }

    @Override
    public void setMsgId(int msgId) {
        super.setMsgId(msgId);
    }

    public String getText() {
        return text;
    }
}
