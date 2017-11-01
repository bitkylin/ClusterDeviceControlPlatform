package cc.bitky.clustermanage.netty.message.tcp;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMsgCardNum;

/**
 * 设备初始化「1, 3」: 发送员工卡号
 */
public class TcpMsgInitResponseCardNumber extends BaseMsgCardNum {

    public TcpMsgInitResponseCardNumber(int groupId, int boxId, String cardNumber) {
        super(groupId, boxId, cardNumber, MsgType.INITIALIZE_DEVICE_RESPONSE_CARD);
    }
}
