package cc.bitky.clustermanage.netty.message.web;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

/**
 * 设备初始化「2」: 服务器下发初始化信息完成
 */
public class WebMsgInitDeployMessageComplete extends BaseMessage {

    public WebMsgInitDeployMessageComplete(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.INITIALIZE_SERVER_DEPLOY_MESSAGE_COMPLETE);
    }
}
