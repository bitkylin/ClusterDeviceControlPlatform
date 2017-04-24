package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMessage;

/**
 * 服务器操作远程开锁
 */
public class WebMsgOperateBoxUnlock extends BaseMessage {

    public WebMsgOperateBoxUnlock(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.SERVER_REMOTE_UNLOCK);
    }
}
