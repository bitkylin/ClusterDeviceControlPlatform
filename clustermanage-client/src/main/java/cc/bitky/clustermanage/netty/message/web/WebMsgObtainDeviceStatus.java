package cc.bitky.clustermanage.netty.message.web;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

/**
 * 服务器获取设备的当前状态
 */
public class WebMsgObtainDeviceStatus extends BaseMessage {


    public WebMsgObtainDeviceStatus(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.SERVER_REQUSET_STATUS);
    }
}
