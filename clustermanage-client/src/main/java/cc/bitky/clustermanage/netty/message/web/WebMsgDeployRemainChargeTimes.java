package cc.bitky.clustermanage.netty.message.web;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

/**
 * 服务器部署剩余充电次数
 */
public class WebMsgDeployRemainChargeTimes extends BaseMessage {

    private int times;

    public WebMsgDeployRemainChargeTimes(int groupId, int boxId, int times) {
        super(groupId, boxId);
        setMsgId(MsgType.SERVER_SET_REMAIN_CHARGE_TIMES);
        this.times = times;
    }

    public int getTimes() {
        return times;
    }
}
