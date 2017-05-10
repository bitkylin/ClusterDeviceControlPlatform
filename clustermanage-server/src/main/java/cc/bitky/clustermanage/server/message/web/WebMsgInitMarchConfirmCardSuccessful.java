package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;

/**
 * 设备初始化「2」: 服务器匹配确认卡号完成
 */
public class WebMsgInitMarchConfirmCardSuccessful extends BaseMessage {

    private boolean successful;

    public WebMsgInitMarchConfirmCardSuccessful(int groupId, int boxId, boolean successful) {
        super(groupId, boxId);
        this.successful = successful;
        setMsgId(MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_SUCCESSFUL);
    }

    public boolean isSuccessful() {
        return successful;
    }
}
