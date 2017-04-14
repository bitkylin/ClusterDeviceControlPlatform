package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMsgCardNum;

/**
 * 服务器部署员工卡号
 */
public class WebMsgDeployEmployeeCardNumber extends BaseMsgCardNum {

    public WebMsgDeployEmployeeCardNumber(int groupId, int boxId, long cardNumber) {
        super(groupId, boxId, cardNumber, MsgType.SERVER_SET_EMPLOYEE_CARD_NUMBER);
    }
}
