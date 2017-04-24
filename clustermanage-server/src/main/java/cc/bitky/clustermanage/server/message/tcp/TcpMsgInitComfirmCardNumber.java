package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMsgCardNum;

/**
 * 设备初始化「3」: 发送确认卡号
 */
public class TcpMsgInitComfirmCardNumber extends BaseMsgCardNum {

    public TcpMsgInitComfirmCardNumber(int groupId, int boxId, long cardNumber) {
        super(groupId, boxId, cardNumber, MsgType.INITIALIZE_DEVICE_RESPONSE_CONFIRM_CARD);
    }
}
