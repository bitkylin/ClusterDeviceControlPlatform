package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class ServerChannelInitializerTest extends ChannelInitializer<NioSocketChannel> {

  @Override protected void initChannel(NioSocketChannel ch) throws Exception {
    ch.pipeline().addLast(new LoggingHandler("NO1"));
    byte head = 0x11;
   // ch.pipeline().addLast(new FrameIdentifierChannelInboundHandler(head));
    ch.pipeline().addLast(new ShowByteBufAsFrameInBoundHandler());
    ch.pipeline().addLast(new LoggingHandler("NO2"));
    ch.pipeline().addLast(new LoggingHandler("NO3"));
//    ch.pipeline().addLast(new LoggingHandler("NO2"));

  }
}
