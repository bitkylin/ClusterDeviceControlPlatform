package cc.bitky.clustermanage.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cc.bitky.clustermanage.global.ServerSetting;
import cc.bitky.clustermanage.server.message.ChargeStatus;
import cc.bitky.clustermanage.server.message.send.SendableMsg;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseStatus;
import cc.bitky.clustermanage.server.schedule.MsgKey;
import cc.bitky.clustermanage.tcp.TcpMediator;
import io.netty.util.HashedWheelTimer;

/**
 * 包含检错重发策略的 CAN 帧发送器
 */
@Service
public class PolicyCanTransmitter {
    private final TcpMediator tcpMediator;
    private ScheduledExecutorService scheduledExecutorService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public PolicyCanTransmitter(TcpMediator tcpMediator) {
        this.tcpMediator = tcpMediator;
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
                    tcpMediator.writeCanToTcp(message);
                    if (message.isResponsive()) {
                        getMsgHashMap().put(message.getMsgKey(), message.getBytes());
                        setWheelTask(message);
                    }
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
                if (message.getSendTimes() >= ServerSetting.AUTO_REPEAT_REQUEST_TIMES) {
                    getMsgHashMap().remove(msgKey);
                    logger.info("时间轮「4」：记录");
                    tcpMediator.handleResDeviceStatus(
                            new TcpMsgResponseStatus(msgKey.getGroupId(), msgKey.getDeviceId(), ChargeStatus.TRAFFIC_ERROR, TcpMsgResponseStatus.ResSource.SERVER));
                } else {
                    logger.info("时间轮「4」：重新设置");
                    write(message);
                }
            } else logger.info("时间轮「3」：成功");
        }, ServerSetting.FRAME_SENT_TO_DETECT_INTERVAL, TimeUnit.SECONDS);
    }

    private HashedWheelTimer getHashedWheelTimer() {
        return tcpMediator.getSendingMsgRepo().getHashedWheelTimer();
    }

    private ConcurrentHashMap<MsgKey, byte[]> getMsgHashMap() {
        return tcpMediator.getSendingMsgRepo().getMsgHashMap();
    }

    private LinkedBlockingDeque<SendableMsg> getLinkedBlockingDeque() {
        return tcpMediator.getSendingMsgRepo().getLinkedBlockingDeque();
    }
}
