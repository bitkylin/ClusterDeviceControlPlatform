package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {

  @Override protected void initChannel(NioSocketChannel ch) throws Exception {
    ch.pipeline().addLast(new LoggingHandler("kyOutlineLogger"));
    ch.pipeline().addLast(new CanFrameChannelInboundHandler());
    ch.pipeline().addLast(new ParsedMessageInBoundHandler());
  }
}
