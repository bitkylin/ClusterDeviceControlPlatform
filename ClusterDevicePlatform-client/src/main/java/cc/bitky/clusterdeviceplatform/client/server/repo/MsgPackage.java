package cc.bitky.clusterdeviceplatform.client.server.repo;

import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;

public class MsgPackage {
    MsgReplyDeviceStatus chargeStatus;
    MsgReplyDeviceStatus workStatus;

    public MsgPackage(MsgReplyDeviceStatus chargeStatus, MsgReplyDeviceStatus workStatus) {
        this.chargeStatus = chargeStatus;
        this.workStatus = workStatus;
    }

    public MsgReplyDeviceStatus getChargeStatus() {
        return chargeStatus;
    }

    public MsgReplyDeviceStatus getWorkStatus() {
        return workStatus;
    }
}
