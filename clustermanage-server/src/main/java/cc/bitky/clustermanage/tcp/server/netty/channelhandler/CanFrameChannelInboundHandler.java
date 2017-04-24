package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.tcp.MsgErrorMessage;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgHeartBeat;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseDeviceStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
@ChannelHandler.Sharable
public class CanFrameChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
  private static final int frameHead = 0x80;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
    if (msg.readableBytes() % 13 != 0) {
      logger.warn("读取到非整数个 CAN 帧");
      return;
    }

    while (msg.readableBytes() >= 13) {
      msg.skipBytes(2);
      int msgId = msg.readByte();
      int boxId = msg.readByte();
      int groupId = msg.readByte();
      IMessage message = handleMessage(msgId, boxId, groupId, msg);
      ctx.fireChannelRead(message);
    }
  }

  private IMessage handleMessage(int msgId, int boxId, int groupId, ByteBuf msg) {
    switch (msgId) {

      case MsgType.DEVICE_RESPONSE_STATUS:
        int status = msg.readByte();
        IMessage msgChargeStatus = new TcpMsgResponseDeviceStatus(groupId, boxId, status);
        msg.skipBytes(7);
        return msgChargeStatus;

      case MsgType.HEART_BEAT:
        IMessage msgHeartBeat = new TcpMsgHeartBeat(groupId);
        msg.skipBytes(8);
        return msgHeartBeat;

      default:
        logger.warn("接收到正确的 CAN 帧，但无法解析");
        return new MsgErrorMessage("接收到正确的 CAN 帧，但无法解析");
    }
  }
}
