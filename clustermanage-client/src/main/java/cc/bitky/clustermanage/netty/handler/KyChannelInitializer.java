package cc.bitky.clustermanage.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class KyChannelInitializer extends ChannelInitializer<SocketChannel> {
    private static KyChannelInitializer kyChannelInitializer;
    private ChannelPipeline pipeline;

    private KyChannelInitializer() {
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
        pipeline.addLast(new ParsedMessageInBoundHandler());
        pipeline.addLast(new KyOutBoundHandler());
    }

    public ChannelPipeline getPipeline() {
        return pipeline;
    }

}