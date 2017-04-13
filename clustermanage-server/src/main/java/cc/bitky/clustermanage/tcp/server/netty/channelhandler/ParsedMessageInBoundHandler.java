package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import cc.bitky.clustermanage.server.bean.KyTcpMessageHandler;
import cc.bitky.clustermanage.server.message.IMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class ParsedMessageInBoundHandler extends SimpleChannelInboundHandler<IMessage> {
  private final KyTcpMessageHandler kyTcpMessageHandler;

  @Autowired
  public ParsedMessageInBoundHandler(KyTcpMessageHandler kyTcpMessageHandler) {
    super();
    this.kyTcpMessageHandler = kyTcpMessageHandler;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, IMessage msg) {
    kyTcpMessageHandler.handleTcpMsg(msg);
  }
}

