package cc.bitky.clustermanage.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseDeviceStatus;
import cc.bitky.clustermanage.view.KyDeviceViewListener;
import cc.bitky.clustermanage.view.MainView;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class KyChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static KyChannelInitializer kyChannelInitializer;
    private final KyOutBoundHandler kyOutBoundHandler;
    private final ParsedMessageInBoundHandler parsedMessageInBoundHandler;
    private ChannelPipeline pipeline;
    private Logger logger = LoggerFactory.getLogger(getClass());

    private KyChannelInitializer() {
        kyOutBoundHandler = new KyOutBoundHandler();
        parsedMessageInBoundHandler = new ParsedMessageInBoundHandler();
        parsedMessageInBoundHandler.setReceiveListener(() -> {
        });
    }


    public static KyChannelInitializer newInstance() {
        if (kyChannelInitializer == null) kyChannelInitializer = new KyChannelInitializer();
        return kyChannelInitializer;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        pipeline = ch.pipeline();
        pipeline.addLast(new LoggingHandler("kyOutlineLogger", LogLevel.INFO));
        pipeline.addLast(new CanFrameChannelInboundHandler());
        pipeline.addLast(parsedMessageInBoundHandler);
        pipeline.addLast(kyOutBoundHandler);
        MainView.getInstance().setListener(new KyDeviceViewListener() {
            @Override
            public void btnChargeChanged(int groupId, int deviceId, int status) {
                pipeline.write(new TcpMsgResponseDeviceStatus(groupId, deviceId, status));
            }
        });
    }

    public ChannelPipeline getPipeline() {
        return pipeline;
    }

}