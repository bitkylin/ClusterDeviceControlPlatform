package cc.bitky.streamadapter.server.netty.channelhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;

public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {

  @Override protected void initChannel(NioSocketChannel ch) throws Exception {
    ch.pipeline().addLast(new LoggingHandler());
    byte head = 0x11;
    ch.pipeline().addLast(new FrameIdentifierChannelInboundHandler(head));
    ch.pipeline().addLast(new MessageIdentifierChannelInboundHandler(CharsetUtil.US_ASCII));
    //ch.pipeline().addLast(new MessageIdentifierChannelInboundHandler());
    ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
      @Override protected void channelRead0(ChannelHandlerContext ctx, String msg)
          throws Exception {
        System.out.println("**************\n");
        System.out.println("**************\n");
        System.out.println("完整帧: ");
        // KyLog.LogFrame(msg);
        System.out.println("收到帧: " + msg);
        System.out.println("\n\n\n**************");
        System.out.println("**************");
      }
    });
  }
}
