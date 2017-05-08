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

import cc.bitky.clustermanage.server.bean.ServerTcpMessageHandler;
import cc.bitky.clustermanage.server.message.PriorityType;
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
    private LinkedBlockingDeque<SendableMsg> linkedBlockingDeque = new LinkedBlockingDeque<>(655360);
    private ScheduledExecutorService scheduledExecutorService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public SendingOutBoundHandler(ServerTcpMessageHandler serverTcpMessageHandler) {
        this.serverTcpMessageHandler = serverTcpMessageHandler;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        //   logger.info("-----------写入队列-------------");
        SendableMsg messages = (SendableMsg) msg;
        if (messages.getPriorityType() == PriorityType.HIGH) {
            linkedBlockingDeque.offerFirst(messages);
        } else {
            linkedBlockingDeque.offerLast(messages);
        }
        initTimer(ctx);
        // HashedWheelTimer


//        if (writeDelayed) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        ctx.writeAndFlush(Unpooled.wrappedBuffer(bytes));
    }

    private void initTimer(ChannelHandlerContext ctx) {
        if (scheduledExecutorService == null) {
            scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
            scheduledExecutorService.scheduleWithFixedDelay(() -> {
                if (!linkedBlockingDeque.isEmpty()) {
                    SendableMsg message = linkedBlockingDeque.poll();
                    getMsgHashMap().put(message.getMsgKey(), message.getBytes());
                    ctx.writeAndFlush(Unpooled.wrappedBuffer(message.getBytes()));
                    setWheelTask(ctx, message);
                }
            }, 0, 50, TimeUnit.MILLISECONDS);
        }
    }

    private void setWheelTask(ChannelHandlerContext ctx, SendableMsg message) {
        logger.info("@%@%设置时间轮：「" + message.getMsgKey() + "」『" + message.getSendTimes() + "』");
        getHashedWheelTimer().newTimeout(timeout -> {
            logger.info("@%@%执行时间轮：「" + message.getMsgKey() + "」『" + message.getSendTimes() + "』");
            MsgKey msgKey = message.getMsgKey();
            if (getMsgHashMap().get(msgKey) != null) {
                logger.info("时间轮：出错");
                message.increaseSendTimes();
                if (message.getSendTimes() >= 3) {
                    getMsgHashMap().remove(msgKey);
                    logger.info("时间轮：记录");
                    serverTcpMessageHandler.handleResDeviceStatus(
                            new TcpMsgResponseStatus(msgKey.getGroupId(), msgKey.getBoxId(), 4, TcpMsgResponseStatus.ResSource.SERVER));
                } else {
                    logger.info("时间轮：重新设置");
                    ctx.writeAndFlush(Unpooled.wrappedBuffer(message.getBytes()));
                    setWheelTask(ctx, message);
                }
            }
        }, 5, TimeUnit.SECONDS);
    }

    private HashedWheelTimer getHashedWheelTimer() {
        return serverTcpMessageHandler.getSendingMsgRepo().getHashedWheelTimer();
    }

    private HashMap<MsgKey, byte[]> getMsgHashMap() {
        return serverTcpMessageHandler.getSendingMsgRepo().getMsgHashMap();
    }
}
