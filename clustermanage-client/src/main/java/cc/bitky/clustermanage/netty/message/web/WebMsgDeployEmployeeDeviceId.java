package cc.bitky.clustermanage.netty.message.web;


import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

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
