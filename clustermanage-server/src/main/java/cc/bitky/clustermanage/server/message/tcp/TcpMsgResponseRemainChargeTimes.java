package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMessage;

/**
 * 设备回复: 剩余充电次数更新
 */
public class TcpMsgResponseRemainChargeTimes extends BaseMessage {

    public TcpMsgResponseRemainChargeTimes(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.DEVICE_RESPONSE_REMAIN_CHARGE_TIMES);
    }
}
