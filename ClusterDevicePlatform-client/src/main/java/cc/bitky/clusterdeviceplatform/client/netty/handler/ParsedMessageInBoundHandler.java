package cc.bitky.clusterdeviceplatform.client.netty.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.client.netty.TcpPresenter;
import cc.bitky.clusterdeviceplatform.messageutils.config.FrameSetting;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.FrameMajor;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.FrameMajorHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Service
@ChannelHandler.Sharable
public class ParsedMessageInBoundHandler extends SimpleChannelInboundHandler<FrameMajor> {

    private final TcpPresenter server;

    @Autowired
    public ParsedMessageInBoundHandler(TcpPresenter server) {
        this.server = server;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FrameMajor msg) throws Exception {
        FrameMajorHeader head = msg.getHead();
        ByteBuf byteBuf = msg.getData();
        while (byteBuf.readableBytes() >= FrameSetting.SUB_FRAME_HEAD_LENGTH) {
            int subMsgId = byteBuf.readByte() & 0xFF;
            byte[] data = new byte[byteBuf.readShort()];
            byteBuf.readBytes(data);
            server.decodeAndHuntMessage(head, subMsgId, data, ctx.channel());
        }
    }
}
