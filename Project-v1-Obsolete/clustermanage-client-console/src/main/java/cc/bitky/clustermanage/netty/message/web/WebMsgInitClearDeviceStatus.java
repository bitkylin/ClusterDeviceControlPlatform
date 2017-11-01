package cc.bitky.clustermanage.netty.message.web;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

/**
 * 设备初始化「3」: 服务器清除设备的初始化状态
 */
public class WebMsgInitClearDeviceStatus extends BaseMessage {

    public WebMsgInitClearDeviceStatus(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.INITIALIZE_SERVER_CLEAR_INITIALIZE_MESSAGE);
    }
}
