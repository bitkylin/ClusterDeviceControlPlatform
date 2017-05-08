package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import cc.bitky.clustermanage.server.bean.ServerTcpMessageHandler;
import cc.bitky.clustermanage.server.message.PriorityType;
import cc.bitky.clustermanage.server.message.send.SendableMsg;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseStatus;
import cc.bitky.clustermanage.server.schedule.MsgKey;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.HashedWheelTimer;

@Service
@ChannelHandler.Sharable
public class SendingOutBoundHandler extends ChannelOutboundHandlerAdapter {
    private final ServerTcpMessageHandler serverTcpMessageHandler;
    LinkedBlockingDeque<SendableMsg> linkedBlockingDeque = new LinkedBlockingDeque<>(655360);
    private Timer timer;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public SendingOutBoundHandler(ServerTcpMessageHandler serverTcpMessageHandler) {
        this.serverTcpMessageHandler = serverTcpMessageHandler;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        logger.info("-----------写入队列-------------");
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
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (!linkedBlockingDeque.isEmpty()) {
                        SendableMsg Message = linkedBlockingDeque.poll();
                        getMsgHashMap().put(Message.getMsgKey(), Message.getBytes());
                        ctx.writeAndFlush(Message.getBytes());
                        setWheelTesk(Message);
                    }
                }
            }, 0, 50);
        }
    }

    private void setWheelTesk(SendableMsg message) {
        getHashedWheelTimer().newTimeout(timeout -> {
            MsgKey msgKey = message.getMsgKey();
            if (getMsgHashMap().get(msgKey) != null) {
                message.increaseSendTimes();
                if (message.getSendTimes() >= 3) {
                    serverTcpMessageHandler.handleResDeviceStatus(
                            new TcpMsgResponseStatus(msgKey.getGroupId(), msgKey.getBoxId(), 4));
                } else {
                    setWheelTesk(message);
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
