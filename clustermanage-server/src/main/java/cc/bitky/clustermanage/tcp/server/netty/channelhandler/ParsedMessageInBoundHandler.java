package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import cc.bitky.clustermanage.server.message.IMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ParsedMessageInBoundHandler extends SimpleChannelInboundHandler<IMessage> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, IMessage msg) {

  }
}

