package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;

/**
 * 服务器部署设备 Id
 */
public class WebMsgDeployEmployeeDeviceId extends BaseMessage {
    int updatedDeviceId;

    public WebMsgDeployEmployeeDeviceId(int groupId, int boxId, int updatedDeviceId) {
        super(groupId, boxId);
        this.updatedDeviceId = updatedDeviceId;
        setMsgId(MsgType.SERVER_SET_DEVICE_ID);
    }

    public int getUpdatedDeviceId() {
        return updatedDeviceId;
    }
}
