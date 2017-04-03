package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.MsgChargeStatus;
import cc.bitky.clustermanage.server.message.MsgErrorMessage;
import cc.bitky.clustermanage.server.message.MsgHeartBeat;
import cc.bitky.clustermanage.tcp.util.enumky.MsgType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.LoggerFactory;

public class CanFrameChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
  private static final int frameHead = 0x80;
  private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
    if (msg.readableBytes() % 13 != 0) {
      logger.warn("读取到非整数个 CAN 帧");
      return;
    }

    while (msg.readableBytes() >= 13) {
      msg.skipBytes(2);
      int func = msg.readByte();
      int boxId = msg.readByte();
      int groupId = msg.readByte();
      IMessage message = handleMessage(func, boxId, groupId, msg);
      ctx.fireChannelRead(message);
    }
  }

  private IMessage handleMessage(int func, int boxId, int groupId, ByteBuf msg) {
    switch (func) {

      case MsgType.CHANGE_STATUS:
        int status = msg.readByte();
        IMessage msgChargeStatus = new MsgChargeStatus(groupId, boxId, status);
        msg.skipBytes(7);
        return msgChargeStatus;

      case MsgType.HEART_BEAT:
        IMessage msgHeartBeat = new MsgHeartBeat(groupId);
        msg.skipBytes(8);
        return msgHeartBeat;

      default:
        logger.warn("接收到正确的 CAN 帧，但无法解析");
        return new MsgErrorMessage("接收到正确的 CAN 帧，但无法解析");
    }
  }
}
