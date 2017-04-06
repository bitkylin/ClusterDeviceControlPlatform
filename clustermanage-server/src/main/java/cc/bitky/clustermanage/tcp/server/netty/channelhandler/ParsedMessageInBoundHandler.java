package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import cc.bitky.clustermanage.server.bean.KyMessageHandler;
import cc.bitky.clustermanage.server.message.IMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParsedMessageInBoundHandler extends SimpleChannelInboundHandler<IMessage> {
  private final KyMessageHandler kyMessageHandler;

  @Autowired
  public ParsedMessageInBoundHandler(KyMessageHandler kyMessageHandler) {
    super();
    this.kyMessageHandler = kyMessageHandler;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, IMessage msg) {
    kyMessageHandler.handle(msg);
  }
}

