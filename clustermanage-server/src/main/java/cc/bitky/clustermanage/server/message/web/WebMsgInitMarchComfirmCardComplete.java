package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMessage;

/**
 * 设备初始化「4」: 服务器匹配确认卡号完成
 */
public class WebMsgInitMarchComfirmCardComplete extends BaseMessage {

    public WebMsgInitMarchComfirmCardComplete(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_SUCCESSFUL);
    }
}
