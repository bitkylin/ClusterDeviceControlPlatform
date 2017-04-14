package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMsgCardNum;

/**
 * 设备初始化「1」: 发送员工卡号
 */
public class TcpMsgInitEmployeeCardNumber extends BaseMsgCardNum {

    public TcpMsgInitEmployeeCardNumber(int groupId, int boxId, long cardNumber) {
        super(groupId, boxId, cardNumber, MsgType.INITIALIZE_DEVICE_RESPONSE_EMPLOYEE_CARD);
    }
}
