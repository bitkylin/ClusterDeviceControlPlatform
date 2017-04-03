package cc.bitky.clustermanage.tcp.server.netty.channelhandler.handlerbackup;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.nio.charset.Charset;
import java.util.List;

public class MessageIdentifierChannelInboundHandler extends MessageToMessageDecoder<ByteBuf> {

  private final Charset charset;

  public MessageIdentifierChannelInboundHandler() {
    this(Charset.defaultCharset());
  }

  public MessageIdentifierChannelInboundHandler(Charset charset) {
    if (charset == null) {
      throw new NullPointerException("charset");
    }
    this.charset = charset;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    out.add(msg.toString(charset));
  }
}


