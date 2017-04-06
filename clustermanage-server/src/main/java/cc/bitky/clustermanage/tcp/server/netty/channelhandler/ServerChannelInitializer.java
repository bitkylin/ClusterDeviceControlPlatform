package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {

  private final CanFrameChannelInboundHandler canFrameChannelInboundHandler;
  private final ParsedMessageInBoundHandler parsedMessageInBoundHandler;

  @Autowired
  public ServerChannelInitializer(CanFrameChannelInboundHandler canFrameChannelInboundHandler,
      ParsedMessageInBoundHandler parsedMessageInBoundHandler) {
    this.canFrameChannelInboundHandler = canFrameChannelInboundHandler;
    this.parsedMessageInBoundHandler = parsedMessageInBoundHandler;
  }

  @Override protected void initChannel(NioSocketChannel ch) throws Exception {
    ch.pipeline().addLast(new LoggingHandler("kyOutlineLogger"));
    ch.pipeline().addLast(canFrameChannelInboundHandler);
    ch.pipeline().addLast(parsedMessageInBoundHandler);
  }
}
