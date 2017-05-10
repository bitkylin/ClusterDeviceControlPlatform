package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;

/**
 * 设备初始化「3」: 服务器清除设备的初始化状态
 */
public class WebMsgInitClearDeviceStatus extends BaseMessage {

    private boolean successful;

    public WebMsgInitClearDeviceStatus(int groupId, int boxId, boolean successful) {
        super(groupId, boxId);
        this.successful = successful;
        setMsgId(MsgType.INITIALIZE_SERVER_CLEAR_INITIALIZE_MESSAGE);
    }

    public boolean isSuccessful() {
        return successful;
    }
}
