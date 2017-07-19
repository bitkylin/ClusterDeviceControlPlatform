package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.springframework.stereotype.Service;

import cc.bitky.clustermanage.server.message.send.SendableMsg;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

@Service
@ChannelHandler.Sharable
public class SendingOutBoundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof SendableMsg)) {
            return;
        }
        SendableMsg message = (SendableMsg) msg;
        ctx.writeAndFlush(Unpooled.wrappedBuffer(message.getBytes()));
    }
}
