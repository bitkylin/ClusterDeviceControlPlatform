package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;

/**
 * 服务器操作远程开锁
 */
public class WebMsgDeployLedStop extends BaseMessage {

    public WebMsgDeployLedStop(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.SERVER_LED_STOP);
    }
}
