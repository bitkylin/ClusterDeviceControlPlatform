package cc.bitky.clusterdeviceplatform.server.tcp.handler;

import cc.bitky.clusterdeviceplatform.messageutils.config.FrameSetting;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.FrameMajor;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.FrameMajorHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 帧识别 - 入站处理器 分割出并向后继处理器传递一个完整的主帧数据体及主帧信息 bean
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class FrameRecognitionInBoundHandler extends SimpleChannelInboundHandler<ByteBuf> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf byteBuf) throws Exception {
        while (true) {
            if (byteBuf.readableBytes() < FrameSetting.FRAME_HEAD_LENGTH) {
                return;
            }
            if (byteBuf.readByte() != FrameSetting.MAJOR_FRAME_HEAD_1
                    || byteBuf.readByte() != FrameSetting.MAJOR_FRAME_HEAD_2) {
                log.warn("数据接收异常「帧头不匹配」");
                return;
            }
            int groupId = byteBuf.readByte() & 0xFF;
            int msgId = byteBuf.readByte() & 0xFF;
            int deviceId = byteBuf.readByte() & 0xFF;
            int backupMsg = byteBuf.readByte() & 0xFF;
            int dataLength = byteBuf.readShort() & 0xFFFF;
            FrameMajorHeader headMsg = new FrameMajorHeader(msgId, groupId, deviceId, dataLength, backupMsg);
            ByteBuf subBuf = ctx.alloc().buffer(dataLength);
            byteBuf.readBytes(subBuf, dataLength);
            ctx.fireChannelRead(new FrameMajor(headMsg, subBuf));
        }
    }
}
