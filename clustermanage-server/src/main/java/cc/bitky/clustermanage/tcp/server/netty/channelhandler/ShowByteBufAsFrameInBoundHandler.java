package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import cc.bitky.clustermanage.tcp.util.KyLog;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ShowByteBufAsFrameInBoundHandler extends SimpleChannelInboundHandler<ByteBuf> {
  @Override protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
    System.out.println("**************\n");
    System.out.println("**************\n");
    System.out.println("完整帧: ");
    KyLog.LogFrame(msg);
    //  System.out.println("收到帧: " + msg);
    System.out.println("\n\n\n**************");
    System.out.println("**************");
    //    DefaultChannelPromise promise = new DefaultChannelPromise(ctx.channel());
    //    promise.addListener(future -> {
    //future.
    //    });

    ctx.pipeline().writeAndFlush(Unpooled.copiedBuffer(msg));//(Unpooled.copiedBuffer(msg));
  }
}

