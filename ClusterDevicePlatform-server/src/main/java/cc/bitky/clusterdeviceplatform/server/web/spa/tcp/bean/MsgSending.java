package cc.bitky.clusterdeviceplatform.server.web.spa.tcp.bean;

import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsgCodec;

public class MsgSending {
    private int groupId;
    private int deviceId;
    private String detail = "";
    private String type = "";

    public MsgSending(BaseMsg msg) {
        groupId = msg.getGroupId();
        deviceId = msg.getDeviceId();
        BaseMsgCodec msgCodec = msg.getMsgCodec();
        if (msgCodec != null) {
            detail = msgCodec.getDetail();
            if (msgCodec.getMajorMsgId() == 0x11) {
                type = "portrait";
            }
        }
    }

    public int getGroupId() {
        return groupId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getDetail() {
        return detail;
    }

    public String getType() {
        return type;
    }
}
