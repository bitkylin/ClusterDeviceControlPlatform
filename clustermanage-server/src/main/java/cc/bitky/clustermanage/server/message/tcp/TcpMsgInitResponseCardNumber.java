package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMsgCardNum;

/**
 * 设备初始化: 发送员工卡号
 */
public class TcpMsgInitResponseCardNumber extends BaseMsgCardNum {

    public TcpMsgInitResponseCardNumber(int groupId, int boxId, long cardNumber) {
        super(groupId, boxId, cardNumber, MsgType.INITIALIZE_DEVICE_RESPONSE_CARD);
    }
}
