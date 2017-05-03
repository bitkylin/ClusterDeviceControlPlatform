package cc.bitky.clustermanage.netty.message.web;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

/**
 * 设备初始化「4」: 服务器匹配确认卡号完成
 */
public class WebMsgInitMarchComfirmCardSuccessful extends BaseMessage {

    public WebMsgInitMarchComfirmCardSuccessful(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_SUCCESSFUL);
    }
}
