package cc.bitky.clustermanage.view;

import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseDeviceStatus;

public interface MainViewActionListener {

    void btnChargeChanged(TcpMsgResponseDeviceStatus tcpMsgResponseDeviceStatus);

    void clearRecCount(boolean rec, boolean err);
}
