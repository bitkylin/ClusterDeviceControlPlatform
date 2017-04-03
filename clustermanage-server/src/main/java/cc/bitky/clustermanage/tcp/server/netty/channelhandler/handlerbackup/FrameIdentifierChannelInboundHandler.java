package cc.bitky.clustermanage.tcp.server.netty.channelhandler.handlerbackup;

import cc.bitky.clustermanage.tcp.util.KyLog;
import cc.bitky.clustermanage.tcp.util.enumky.FrameReceivedEnum;
import cc.bitky.clustermanage.tcp.util.exception.FrameLoadException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 入端自定义帧提取处理器
 * 将数据流提取出完整的自定义帧并传入下一个处理器
 */
public class FrameIdentifierChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
  private byte[] frameHead;
  private int frameHeadLength;
  private int frameBodyLength;
  private FrameReceivedEnum frameStatus = FrameReceivedEnum.READY;
  private ByteBuf holdByteBuf = Unpooled.buffer(1024);

  private FrameIdentifierChannelInboundHandler() {
    super();
  }

  FrameIdentifierChannelInboundHandler(byte... frameHead) {
    this();
    this.frameHead = frameHead;
    frameHeadLength = frameHead.length;
  }

  @Override protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
      throws FrameLoadException {
    System.out.println("****************");
    KyLog.LogFrame(holdByteBuf);
    //数据读入本地buffer
    KyLog.LogFrame(msg);
    holdByteBuf.writeBytes(msg);
    KyLog.LogFrame(holdByteBuf);
    System.out.println("****************\n\n\n\n\n");

    while (true) {

      //若读取状态为: 开始读取
      if (frameStatus == FrameReceivedEnum.READY) {
        if (!matchFrameHead(holdByteBuf)) {
          holdByteBuf.clear();
          break;
        }
      }

      //若读取状态为: 帧长读取
      //数据体完全包含在buffer内，则可通过此状态
      if (frameStatus == FrameReceivedEnum.READING_LENGTH) {
        if (holdByteBuf.readableBytes() <= 1) break;
        //无符号short需要用int型引用
        int currentFrameLength = holdByteBuf.getUnsignedShort(holdByteBuf.readerIndex());

        //可读byte数为长度计数(2)+数据体长度; 所以当前帧长+2 <= 可读帧长
        if (currentFrameLength + 2 <= holdByteBuf.readableBytes()) {
          frameBodyLength = holdByteBuf.readUnsignedShort();
          frameStatus = FrameReceivedEnum.READING_BODY;
        } else {
          break;
        }
      }

      //若读取状态为: 数据体读取
      //预设数据体完全包含在buffer内，否则抛出异常
      if (frameStatus == FrameReceivedEnum.READING_BODY) {
        if (frameBodyLength == 0) {
          frameStatus = FrameReceivedEnum.READY;
          frameBodyLength = -1;
          holdByteBuf.discardReadBytes();
        } else if (frameBodyLength > 0) {
          ByteBuf returnBuf = Unpooled.buffer(frameBodyLength);
          holdByteBuf.readBytes(returnBuf);
          frameStatus = FrameReceivedEnum.READY;
          //    ctx.fireChannelRead(returnBuf);
          ctx.writeAndFlush(returnBuf);
          frameBodyLength = -1;
          holdByteBuf.discardReadBytes();
        } else {
          throw new FrameLoadException("自定义帧长度计数异常");
        }
      } else {
        throw new FrameLoadException("自定义帧读取异常");
      }
    }
  }

  private boolean matchFrameHead(ByteBuf byteBuf) {
    while (true) {
      if (byteBuf.readableBytes() < frameHeadLength) {
        return false;
      }
      if (frameHead[0] == byteBuf.readByte()) {
        frameStatus = FrameReceivedEnum.READING_LENGTH;
        return true;
      }
    }
  }
}
