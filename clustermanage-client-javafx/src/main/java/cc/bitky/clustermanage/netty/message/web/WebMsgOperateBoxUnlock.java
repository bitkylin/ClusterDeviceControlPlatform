package cc.bitky.clustermanage.netty.message.web;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

/**
 * 服务器操作远程开锁
 */
public class WebMsgOperateBoxUnlock extends BaseMessage {

    public WebMsgOperateBoxUnlock(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.SERVER_REMOTE_UNLOCK);
    }
}
