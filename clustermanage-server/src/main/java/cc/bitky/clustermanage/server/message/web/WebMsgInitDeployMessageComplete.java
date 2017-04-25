package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;

/**
 * 设备初始化「2」: 服务器下发初始化信息完成
 */
public class WebMsgInitDeployMessageComplete extends BaseMessage {

    public WebMsgInitDeployMessageComplete(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.INITIALIZE_SERVER_DEPLOY_MESSAGE_COMPLETE);
    }
}
