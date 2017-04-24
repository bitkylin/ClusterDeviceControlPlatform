package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMessage;

/**
 * 服务器获取设备的当前状态
 */
public class WebMsgObtainDeviceStatus extends BaseMessage {


    public WebMsgObtainDeviceStatus(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.SERVER_REQUSET_STATUS);
    }
}
