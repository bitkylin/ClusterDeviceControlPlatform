package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import cc.bitky.clustermanage.tcp.clienttest.ClientTest;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {
  private final CanFrameChannelInboundHandler canFrameChannelInboundHandler;
  private final ParsedMessageInBoundHandler parsedMessageInBoundHandler;
  private final WebMsgOutBoundHandler webMsgOutBoundHandler;
  private ChannelPipeline pipeline;

  @Autowired
  public ServerChannelInitializer(CanFrameChannelInboundHandler canFrameChannelInboundHandler,
      ParsedMessageInBoundHandler parsedMessageInBoundHandler,
      WebMsgOutBoundHandler webMsgOutBoundHandler, ClientTest clientTest) {
    this.canFrameChannelInboundHandler = canFrameChannelInboundHandler;
    this.parsedMessageInBoundHandler = parsedMessageInBoundHandler;
    this.webMsgOutBoundHandler = webMsgOutBoundHandler;
    //   this.clientTest = clientTest;
  }
  // private ClientTest clientTest;

  public ChannelPipeline getPipeline() {
    return pipeline;
  }

  @Override
  protected void initChannel(NioSocketChannel ch) throws Exception {
    pipeline = ch.pipeline();
    ch.pipeline().addLast(new LoggingHandler("kyOutlineLogger", LogLevel.INFO));
    ch.pipeline().addLast(canFrameChannelInboundHandler);
    ch.pipeline().addLast(parsedMessageInBoundHandler);
    ch.pipeline().addLast(webMsgOutBoundHandler);
  }
}
