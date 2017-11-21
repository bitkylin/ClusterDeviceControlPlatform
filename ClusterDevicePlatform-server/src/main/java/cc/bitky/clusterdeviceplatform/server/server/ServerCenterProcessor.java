package cc.bitky.clusterdeviceplatform.server.server;

import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.messageutils.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.messageutils.define.BaseMsg;
import cc.bitky.clusterdeviceplatform.server.config.WebSetting;

@Service
public class ServerCenterProcessor {

    private ServerTcpProcessor tcpProcessor;

    private boolean sendMessage(BaseMsg message) {
        return message != null && tcpProcessor.sendMessage(message);
    }

    boolean sendMessageGrouped(BaseMsg message) {
        int groupId = message.getGroupId();
        int deviceId = message.getDeviceId();
        boolean success = true;

        if (groupId == WebSetting.BROADCAST_GROUP_ID && deviceId == WebSetting.BROADCAST_DEVICE_ID) {
            for (int tempGroupId = 1; tempGroupId <= DeviceSetting.MAX_GROUP_ID; tempGroupId++) {
                for (int tempDeviceId = 1; tempDeviceId <= DeviceSetting.MAX_DEVICE_ID; tempDeviceId++) {
                    if (!sendMessage(message.clone(tempGroupId, tempDeviceId))) {
                        success = false;
                    }
                }
            }
            return success;
        }

        if (groupId == WebSetting.BROADCAST_GROUP_ID) {
            for (int tempGroupId = 1; tempGroupId <= DeviceSetting.MAX_GROUP_ID; tempGroupId++) {
                if (!sendMessage(message.clone(tempGroupId, deviceId))) {
                    success = false;
                }
            }
            return success;
        }

        if (deviceId == WebSetting.BROADCAST_DEVICE_ID) {
            for (int tempDeviceId = 1; tempDeviceId <= DeviceSetting.MAX_DEVICE_ID; tempDeviceId++) {
                if (!sendMessage(message.clone(groupId, tempDeviceId))) {
                    success = false;
                }
            }
            return success;
        }

        return sendMessage(message);
    }

    void setTcpProcessor(ServerTcpProcessor tcpProcessor) {
        this.tcpProcessor = tcpProcessor;
    }
}
