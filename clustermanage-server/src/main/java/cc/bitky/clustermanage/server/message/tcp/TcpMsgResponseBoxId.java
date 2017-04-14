package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMessage;

/**
 * 设备回复: 设备 Id 更新
 */
public class TcpMsgResponseBoxId extends BaseMessage {

    public TcpMsgResponseBoxId(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.DEVICE_RESPONSE_DEVICE_ID);
    }
}
