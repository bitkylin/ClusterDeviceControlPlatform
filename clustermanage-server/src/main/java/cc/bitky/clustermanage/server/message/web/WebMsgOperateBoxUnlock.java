package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;

/**
 * 服务器操作远程开锁
 */
public class WebMsgOperateBoxUnlock extends BaseMessage {

    public WebMsgOperateBoxUnlock(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.SERVER_REMOTE_UNLOCK);
    }


    public WebMsgOperateBoxUnlock kyClone(int groupId) {
        return new WebMsgOperateBoxUnlock(groupId, getDeviceId());
    }
}
