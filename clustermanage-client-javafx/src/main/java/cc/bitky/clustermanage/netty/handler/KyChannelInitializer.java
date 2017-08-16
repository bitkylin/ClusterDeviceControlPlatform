package cc.bitky.clustermanage.netty.handler;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import cc.bitky.clustermanage.netty.message.TcpMsgResponseRandomDeviceStatus;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseDeviceStatus;
import cc.bitky.clustermanage.view.MainView;
import cc.bitky.clustermanage.view.MainViewActionListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class KyChannelInitializer extends ChannelInitializer<SocketChannel> {
    private final LinkedBlockingDeque<TcpMsgResponseDeviceStatus> linkedBlockingDeque = new LinkedBlockingDeque<>(655360);
    ParsedMessageInBoundHandler parsedMessageInBoundHandler = new ParsedMessageInBoundHandler();
    Random random = new Random();

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new LoggingHandler("kyOutlineLogger", LogLevel.INFO));
        pipeline.addLast(new CanFrameChannelInboundHandler());
        pipeline.addLast(parsedMessageInBoundHandler);
        pipeline.addLast(new KyOutBoundHandler());
        MainView.getInstance().setListener(new MainViewActionListener() {
            @Override
            public void btnChargeChanged(TcpMsgResponseDeviceStatus tcpMsgResponseDeviceStatus) {
                pipeline.write(tcpMsgResponseDeviceStatus);
            }

            @Override
            public void btnRandomChargeChanged(TcpMsgResponseRandomDeviceStatus tcpMsgResponseDeviceStatus) {
                randomChargeChanged(tcpMsgResponseDeviceStatus);
            }

            @Override
            public void clearRecCount(boolean rec, boolean err) {
                parsedMessageInBoundHandler.clearRecCount(rec, err);
            }

            void randomChargeChanged(TcpMsgResponseRandomDeviceStatus randomDeviceStatus) {
                if (linkedBlockingDeque.size() > 100000) return;
                int length = randomDeviceStatus.getLength();
                for (int i = 0; i < length; i++) {
                    int groupId = getRandomInt(1, randomDeviceStatus.getGroupId());
                    int deviceId = getRandomInt(1, 100);
                    int status = getRandomInt(1, randomDeviceStatus.getStatusMax());
                    linkedBlockingDeque.add(new TcpMsgResponseDeviceStatus(groupId, deviceId, status));
                }
                Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(() -> {
                    if (!linkedBlockingDeque.isEmpty()) {
                        TcpMsgResponseDeviceStatus message = linkedBlockingDeque.poll();
                        pipeline.channel().writeAndFlush(message);
                    }
                }, 0, 20, TimeUnit.MILLISECONDS);
            }
        });
    }

    int getRandomInt(int min, int max) {
        return random.nextInt(max) % (max - min + 1) + min;
    }
}