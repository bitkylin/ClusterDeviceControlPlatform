package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMessage;

/**
 * 设备回复: 开锁成功
 */
public class TcpMsgResponseOperateBoxUnlock extends BaseMessage {

    public TcpMsgResponseOperateBoxUnlock(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.DEVICE_RESPONSE_REMOTE_UNLOCK);
    }
}
