package cc.bitky.clustermanage.netty.handler;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseTcpResponseMsg;
import cc.bitky.clustermanage.netty.message.base.IMessage;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgInitResponseCardNumber;
import cc.bitky.clustermanage.utils.TcpMsgBuilder;
import cc.bitky.clustermanage.utils.TcpSendListener;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class KyOutBoundHandler extends ChannelOutboundHandlerAdapter {

    private final TcpMsgBuilder tcpMsgBuilder = new TcpMsgBuilder();
    private TcpSendListener sendListener;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        IMessage message = (IMessage) msg;
        if (message.getMsgId() >= 0x40 && message.getMsgId() <= 0x4F) {
            ctx.writeAndFlush(Unpooled.wrappedBuffer(tcpMsgBuilder.buildResponseMsg((BaseTcpResponseMsg) message)));
        } else if (message.getMsgId() >= 0x60 && message.getMsgId() <= 0x6F) {
            ctx.writeAndFlush(Unpooled.wrappedBuffer(tcpMsgBuilder.buildResponseMsg((BaseTcpResponseMsg) message)));
        } else if (message.getMsgId() >= -128 && message.getMsgId() <= -113) {
            ctx.writeAndFlush(Unpooled.wrappedBuffer(tcpMsgBuilder.buildResponseMsg((BaseTcpResponseMsg) message)));
        } else if (message.getMsgId() == MsgType.INITIALIZE_DEVICE_RESPONSE_CARD)
            ctx.writeAndFlush(Unpooled.wrappedBuffer(tcpMsgBuilder.buildInitConfirmCardNumber((TcpMsgInitResponseCardNumber) message)));
        if (message.getMsgId() == 0x00) {
            ctx.writeAndFlush(Unpooled.wrappedBuffer(tcpMsgBuilder.buildResponseMsg((BaseTcpResponseMsg) message)));
        }
        if (sendListener != null) sendListener.send();
    }

    void setSendListener(TcpSendListener sendListener) {
        this.sendListener = sendListener;
    }
}
