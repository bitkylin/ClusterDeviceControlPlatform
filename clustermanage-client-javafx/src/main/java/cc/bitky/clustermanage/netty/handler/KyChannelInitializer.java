package cc.bitky.clustermanage.netty.handler;

import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseDeviceStatus;
import cc.bitky.clustermanage.view.MainViewActionListener;
import cc.bitky.clustermanage.view.MainView;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class KyChannelInitializer extends ChannelInitializer<SocketChannel> {
    ParsedMessageInBoundHandler parsedMessageInBoundHandler = new ParsedMessageInBoundHandler();

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
            public void clearRecCount(boolean rec, boolean err) {
                parsedMessageInBoundHandler.clearRecCount( rec,  err);
            }
        });
    }
}