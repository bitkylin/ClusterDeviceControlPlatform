package cc.bitky.clustermanage.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private long startStamp;

    private KyChannelInitializer() {
        kyOutBoundHandler = new KyOutBoundHandler();
        parsedMessageInBoundHandler = new ParsedMessageInBoundHandler();

        kyOutBoundHandler.setSendListener(() -> {
            startStamp = System.currentTimeMillis();
        });

        parsedMessageInBoundHandler.setReceiveListener(() -> {
            logger.info("持续时间 " + (System.currentTimeMillis() - startStamp) + " ms");
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
    }

    public ChannelPipeline getPipeline() {
        return pipeline;
    }

}