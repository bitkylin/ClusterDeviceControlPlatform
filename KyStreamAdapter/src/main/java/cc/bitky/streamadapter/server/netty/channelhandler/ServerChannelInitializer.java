package cc.bitky.streamadapter.server.netty.channelhandler;

import cc.bitky.streamadapter.util.KyLog;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {

  @Override protected void initChannel(NioSocketChannel ch) throws Exception {
    ch.pipeline().addLast(new LoggingHandler());
    byte head = 0x11;
    ch.pipeline().addLast(new FrameIdentifierChannelInboundHandler(head));
    ch.pipeline().addLast(new MessageIdentifierChannelInboundHandler());
    ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
      @Override protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
          throws Exception {
        System.out.println("**************\n");
        System.out.println("**************\n");
        System.out.println("完整帧: ");
        KyLog.LogFrame(msg);
        System.out.println("\n\n\n**************");
        System.out.println("**************");
      }
    });
  }
}
