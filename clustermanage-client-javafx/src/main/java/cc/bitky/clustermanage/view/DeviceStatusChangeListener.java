package cc.bitky.clustermanage.view;

import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseDeviceStatus;

public interface DeviceStatusChangeListener {

    void btnChargeChanged(TcpMsgResponseDeviceStatus tcpMsgResponseDeviceStatus);
}
