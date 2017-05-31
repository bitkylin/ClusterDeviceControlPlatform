package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import cc.bitky.clustermanage.server.bean.ServerTcpMessageHandler;
import cc.bitky.clustermanage.server.message.send.SendableMsg;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Service
public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private final ConfigInBoundHandler configInBoundHandler;
    private final CanFrameChannelInboundHandler canFrameChannelInboundHandler;
    private final ParsedMessageInBoundHandler parsedMessageInBoundHandler;
    private final WebMsgOutBoundHandler webMsgOutBoundHandler;
    private final SendingOutBoundHandler sendingOutBoundHandler;
    private final QueuingOutBoundHandler queuingOutBoundHandler;
    private final ServerTcpMessageHandler serverTcpMessageHandler;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ServerChannelInitializer(ServerTcpMessageHandler serverTcpMessageHandler,
                                    ConfigInBoundHandler configInBoundHandler,
                                    CanFrameChannelInboundHandler canFrameChannelInboundHandler,
                                    ParsedMessageInBoundHandler parsedMessageInBoundHandler,
                                    WebMsgOutBoundHandler webMsgOutBoundHandler,
                                    QueuingOutBoundHandler queuingOutBoundHandler,
                                    SendingOutBoundHandler sendingOutBoundHandler) {

        this.serverTcpMessageHandler = serverTcpMessageHandler;
        this.configInBoundHandler = configInBoundHandler;
        this.canFrameChannelInboundHandler = canFrameChannelInboundHandler;
        this.parsedMessageInBoundHandler = parsedMessageInBoundHandler;
        this.webMsgOutBoundHandler = webMsgOutBoundHandler;
        this.queuingOutBoundHandler = queuingOutBoundHandler;
        this.sendingOutBoundHandler = sendingOutBoundHandler;

        queuingOutBoundHandler.setServerChannelInitializer(this);
        parsedMessageInBoundHandler.setServerChannelInitializer(this);
        configInBoundHandler.setServerChannelInitializer(this);
    }

    ServerTcpMessageHandler getServerTcpMessageHandler() {
        return serverTcpMessageHandler;
    }

    void writeToTcp(SendableMsg sendableMsg) {
        List<ChannelPipeline> channelPipelines = getServerTcpMessageHandler().getSendingMsgRepo().getChannelPipelines();
        if (channelPipelines.size() == 0) return;
        channelPipelines.forEach(pipe -> pipe.writeAndFlush(sendableMsg));
    }

    @Override
    protected void initChannel(NioSocketChannel ch) {
        ch.pipeline().addLast(new LoggingHandler("kyOutlineLogger", LogLevel.INFO));
        ch.pipeline().addLast(configInBoundHandler);
        ch.pipeline().addLast(canFrameChannelInboundHandler);
        ch.pipeline().addLast(parsedMessageInBoundHandler);
        ch.pipeline().addLast(sendingOutBoundHandler);

        getServerTcpMessageHandler().setSendWebMessagesListener(iMessages -> {
                    if (getServerTcpMessageHandler().getSendingMsgRepo().getChannelPipelines().size() > 0) {
                        webMsgOutBoundHandler.write(iMessages);
                        return true;
                    }
                    return false;
                }
        );
    }
}
