package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Service
public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    private final CanFrameChannelInboundHandler canFrameChannelInboundHandler;
    private final ParsedMessageInBoundHandler parsedMessageInBoundHandler;
    private final WebMsgOutBoundHandler webMsgOutBoundHandler;
    private final SendingOutBoundHandler sendingOutBoundHandler;
    private final List<NioSocketChannel> nioSocketChannels = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ServerChannelInitializer(CanFrameChannelInboundHandler canFrameChannelInboundHandler,
                                    ParsedMessageInBoundHandler parsedMessageInBoundHandler,
                                    WebMsgOutBoundHandler webMsgOutBoundHandler,
                                    SendingOutBoundHandler sendingOutBoundHandler) {

        this.canFrameChannelInboundHandler = canFrameChannelInboundHandler;
        this.parsedMessageInBoundHandler = parsedMessageInBoundHandler;
        this.webMsgOutBoundHandler = webMsgOutBoundHandler;
        this.sendingOutBoundHandler = sendingOutBoundHandler;
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        nioSocketChannels.add(ch);
        ch.pipeline().addLast(new LoggingHandler("kyOutlineLogger", LogLevel.INFO));
        ch.pipeline().addLast(canFrameChannelInboundHandler);
        ch.pipeline().addLast(parsedMessageInBoundHandler);
        ch.pipeline().addLast(sendingOutBoundHandler);
        ch.pipeline().addLast(webMsgOutBoundHandler);
     //   ch.pipeline().addLast(new LoggingHandler("kyOutlineLogger2", LogLevel.INFO));

        parsedMessageInBoundHandler.getServerTcpMessageHandler()
                .setSendWebMessagesListener(iMessages -> {
                            boolean success = false;
                            Iterator<NioSocketChannel> iterator = nioSocketChannels.iterator();
                            while (iterator.hasNext()) {
                                NioSocketChannel channel = iterator.next();
                                if (channel == null || channel.isShutdown())
                                    iterator.remove();
                                else {
                                    channel.pipeline().write(iMessages);
                                    success = true;
                                }
                            }
                            return success;
                        }
                );
    }  //     logger.warn("Netty 模块未初始化，无通道可使用");
}
