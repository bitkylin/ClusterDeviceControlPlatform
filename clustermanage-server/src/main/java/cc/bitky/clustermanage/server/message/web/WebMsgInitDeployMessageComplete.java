package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMsgCardNum;

/**
 * 设备初始化「2」: 服务器下发初始化信息完成
 */
public class WebMsgInitDeployMessageComplete extends BaseMsgCardNum {

    public WebMsgInitDeployMessageComplete(int groupId, int boxId, long cardNumber) {
        super(groupId, boxId, cardNumber, MsgType.INITIALIZE_SERVER_DEPLOY_MESSAGE_COMPLETE);
    }
}
