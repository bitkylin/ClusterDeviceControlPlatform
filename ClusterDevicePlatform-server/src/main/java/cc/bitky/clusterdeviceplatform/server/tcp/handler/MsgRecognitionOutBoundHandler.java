package cc.bitky.clusterdeviceplatform.server.tcp.handler;

import org.springframework.stereotype.Service;

import java.util.List;

import cc.bitky.clusterdeviceplatform.messageutils.config.FrameSetting;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.FrameMajorHeader;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.SendableMsgContainer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 已编码的 TCP 帧资源容器，转化为可发送的 TCP 主帧
 */
@Service
@ChannelHandler.Sharable
public class MsgRecognitionOutBoundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        SendableMsgContainer message = (SendableMsgContainer) msg;
        ByteBuf byteBuf = ctx.alloc().buffer();
        FrameMajorHeader header = message.getFrameHeader();
        byteBuf.writeByte(FrameSetting.MAJOR_FRAME_HEAD_1);
        byteBuf.writeByte(FrameSetting.MAJOR_FRAME_HEAD_2);
        byteBuf.writeByte(header.getGroupId());
        byteBuf.writeByte(header.getMsgId());
        byteBuf.writeByte(header.getDeviceId());
        byteBuf.writeByte(header.getBackupMsg());
        byteBuf.writeShort(header.getDataLength());

        List<ByteBuf> dataList = message.getDataList();
        dataList.forEach(byteBuf::writeBytes);
        ctx.writeAndFlush(byteBuf);
    }
}
