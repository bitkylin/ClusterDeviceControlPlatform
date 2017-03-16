package cc.bitky.streamadapter.server.netty.channelhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class MessageIdentifierChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
  @Override protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {

  }
}
