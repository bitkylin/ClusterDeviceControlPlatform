package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMsgCardNum;

/**
 * 设备初始化「4」: 服务器匹配确认卡号完成
 */
public class WebMsgInitMarchComfirmCardComplete extends BaseMsgCardNum {

    public WebMsgInitMarchComfirmCardComplete(int groupId, int boxId, long cardNumber) {
        super(groupId, boxId, cardNumber, MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_SUCCESSFUL);
    }
}
