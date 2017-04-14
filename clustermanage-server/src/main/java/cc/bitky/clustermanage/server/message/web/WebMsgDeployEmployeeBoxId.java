package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.BaseMessage;

/**
 * 服务器部署设备 Id
 */
class WebMsgDeployEmployeeBoxId extends BaseMessage {
    int newBoxId;

    public WebMsgDeployEmployeeBoxId(int groupId, int boxId, int newBoxId) {
        super(groupId, boxId);
        this.newBoxId = newBoxId;
        setMsgId(MsgType.SERVER_SET_DEVICE_ID);
    }

    public int getNewBoxId() {
        return newBoxId;
    }
}
