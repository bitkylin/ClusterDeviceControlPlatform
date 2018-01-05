package cc.bitky.clusterdeviceplatform.server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.server.config.CommSetting;
import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.config.WebSetting;

@Service
public class ServerCenterProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ServerCenterProcessor.class);
    private ServerTcpProcessor tcpProcessor;

    public ServerCenterProcessor() {
        jvmShutDown();
    }

    public ServerTcpProcessor getTcpProcessor() {
        return tcpProcessor;
    }

    void setTcpProcessor(ServerTcpProcessor tcpProcessor) {
        this.tcpProcessor = tcpProcessor;
    }

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

    private void jvmShutDown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.warn("开始关闭服务器");
            tcpProcessor.shutDown();
            try {
                Thread.sleep(CommSetting.ACCESSIBLE_CHANNEL_REPLY_INTERVAL);
            } catch (InterruptedException ignored) {
            }
            logger.warn("服务器优雅关闭成功");
        }));
    }
}
