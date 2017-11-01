package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Service
public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private final ConfigHandler configHandler;
    private final CanFrameChannelInboundHandler canFrameChannelInboundHandler;
    private final ParsedMessageInBoundHandler parsedMessageInBoundHandler;
    private final SendingOutBoundHandler sendingOutBoundHandler;

    @Autowired
    public ServerChannelInitializer(ConfigHandler configHandler,
                                    CanFrameChannelInboundHandler canFrameChannelInboundHandler,
                                    ParsedMessageInBoundHandler parsedMessageInBoundHandler,
                                    SendingOutBoundHandler sendingOutBoundHandler) {

        this.configHandler = configHandler;
        this.canFrameChannelInboundHandler = canFrameChannelInboundHandler;
        this.parsedMessageInBoundHandler = parsedMessageInBoundHandler;
        this.sendingOutBoundHandler = sendingOutBoundHandler;
    }


    @Override
    protected void initChannel(NioSocketChannel ch) {
        ch.pipeline().addLast(new LoggingHandler("kyOutlineLogger", LogLevel.INFO));
        ch.pipeline().addLast(configHandler);
        ch.pipeline().addLast(canFrameChannelInboundHandler);
        ch.pipeline().addLast(parsedMessageInBoundHandler);
        ch.pipeline().addLast(sendingOutBoundHandler);
    }
}
