package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cc.bitky.clustermanage.global.ServerSetting;
import cc.bitky.clustermanage.server.bean.ServerTcpMessageHandler;
import cc.bitky.clustermanage.server.message.send.SendableMsg;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseStatus;
import cc.bitky.clustermanage.server.schedule.MsgKey;
import io.netty.channel.ChannelHandler;
import io.netty.util.HashedWheelTimer;

@Service
@ChannelHandler.Sharable
public class QueuingOutBoundHandler {
    private  ServerChannelInitializer serverChannelInitializer;
    private ScheduledExecutorService scheduledExecutorService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    void setServerChannelInitializer(ServerChannelInitializer serverChannelInitializer) {
        this.serverChannelInitializer = serverChannelInitializer;
    }

    private ServerTcpMessageHandler getServerTcpMessageHandler() {
        return serverChannelInitializer.getServerTcpMessageHandler();
    }

    void write(SendableMsg message) {
        if (message.isUrgent()) {
            getLinkedBlockingDeque().offerFirst(message);
        } else {
            getLinkedBlockingDeque().offerLast(message);
        }
        initTimer();
    }

    private void initTimer() {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleWithFixedDelay(() -> {
                        if (!getLinkedBlockingDeque().isEmpty()) {
                            SendableMsg message = getLinkedBlockingDeque().poll();
                            getMsgHashMap().put(message.getMsgKey(), message.getBytes());
                            serverChannelInitializer.writeToTcp(message);
                            if (message.isResponsive())
                                setWheelTask(message);
                        }
                    }, 0, ServerSetting.FRAME_SEND_INTERVAL, TimeUnit.MILLISECONDS);
        }
    }

    private void setWheelTask(SendableMsg message) {
        logger.info("@%@%「1」设置时间轮：「" + message.getMsgKey() + "」『" + message.getSendTimes() + "』");
        getHashedWheelTimer().newTimeout(timeout -> {
            logger.info("@%@%「2」执行时间轮：「" + message.getMsgKey() + "」『" + message.getSendTimes() + "』");
            MsgKey msgKey = message.getMsgKey();
            if (getMsgHashMap().get(msgKey) != null) {
                logger.info("时间轮「3」：出错");
                message.increaseSendTimes();
                if (message.getSendTimes() >= 3) {
                    getMsgHashMap().remove(msgKey);
                    logger.info("时间轮「4」：记录");
                    getServerTcpMessageHandler().handleResDeviceStatus(
                            new TcpMsgResponseStatus(msgKey.getGroupId(), msgKey.getDeviceId(), 5, TcpMsgResponseStatus.ResSource.SERVER));
                } else {
                    logger.info("时间轮「4」：重新设置");
                    if (message.isUrgent()) {
                        getLinkedBlockingDeque().offerFirst(message);
                    } else {
                        getLinkedBlockingDeque().offerLast(message);
                    }
                    setWheelTask(message);
                }
            } else logger.info("时间轮「3」：成功");
        }, ServerSetting.FRAME_SENT_TO_DETECT_INTERVAL, TimeUnit.SECONDS);
    }

    private HashedWheelTimer getHashedWheelTimer() {
        return getServerTcpMessageHandler().getSendingMsgRepo().getHashedWheelTimer();
    }

    private HashMap<MsgKey, byte[]> getMsgHashMap() {
        return getServerTcpMessageHandler().getSendingMsgRepo().getMsgHashMap();
    }

    private LinkedBlockingDeque<SendableMsg> getLinkedBlockingDeque() {
        return getServerTcpMessageHandler().getSendingMsgRepo().getLinkedBlockingDeque();
    }
}
