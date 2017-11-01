package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMsgCardNum;

/**
 * 服务器部署员工卡号
 */
public class WebMsgDeployEmployeeCardNumber extends BaseMsgCardNum {

    public WebMsgDeployEmployeeCardNumber(int groupId, int boxId, String cardNumber) {
        super(groupId, boxId, cardNumber, MsgType.SERVER_SET_EMPLOYEE_CARD_NUMBER);
    }
}
