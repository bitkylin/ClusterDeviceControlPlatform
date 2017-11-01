package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseTcpResponseMsg;

/**
 * 设备回复: 剩余充电次数更新
 */
public class TcpMsgResponseRemainChargeTimes extends BaseTcpResponseMsg {

    public TcpMsgResponseRemainChargeTimes(int groupId, int boxId, int status) {
        super(groupId, boxId, status);
        setMsgId(MsgType.DEVICE_RESPONSE_REMAIN_CHARGE_TIMES);
    }
}
