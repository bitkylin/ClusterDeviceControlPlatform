package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import cc.bitky.clustermanage.ServerSetting;
import cc.bitky.clustermanage.server.bean.ServerTcpMessageHandler;
import cc.bitky.clustermanage.server.message.send.SendableMsg;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseStatus;
import cc.bitky.clustermanage.server.schedule.MsgKey;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.HashedWheelTimer;

@Service
@ChannelHandler.Sharable
public class SendingOutBoundHandler extends ChannelOutboundHandlerAdapter {
    private final ServerTcpMessageHandler serverTcpMessageHandler;
    private ScheduledExecutorService scheduledExecutorService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public SendingOutBoundHandler(ServerTcpMessageHandler serverTcpMessageHandler) {
        this.serverTcpMessageHandler = serverTcpMessageHandler;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        SendableMsg message = (SendableMsg) msg;
        if (message.isUrgent()) {
            getLinkedBlockingDeque().offerFirst(message);
        } else {
            getLinkedBlockingDeque().offerLast(message);
        }
        initTimer(ctx);
    }

    private void initTimer(ChannelHandlerContext ctx) {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleWithFixedDelay(() -> {
                if (!getLinkedBlockingDeque().isEmpty()) {
                    SendableMsg message = getLinkedBlockingDeque().poll();
                    getMsgHashMap().put(message.getMsgKey(), message.getBytes());
                    ctx.writeAndFlush(Unpooled.wrappedBuffer(message.getBytes()));
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
                    serverTcpMessageHandler.handleResDeviceStatus(
                            new TcpMsgResponseStatus(msgKey.getGroupId(), msgKey.getBoxId(), 5, TcpMsgResponseStatus.ResSource.SERVER));
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
        return serverTcpMessageHandler.getSendingMsgRepo().getHashedWheelTimer();
    }

    private HashMap<MsgKey, byte[]> getMsgHashMap() {
        return serverTcpMessageHandler.getSendingMsgRepo().getMsgHashMap();
    }

    private LinkedBlockingDeque<SendableMsg> getLinkedBlockingDeque() {
        return serverTcpMessageHandler.getSendingMsgRepo().getLinkedBlockingDeque();
    }
}
