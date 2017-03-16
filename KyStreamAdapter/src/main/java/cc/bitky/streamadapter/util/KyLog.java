package cc.bitky.streamadapter.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * Created by bitky on 2017/3/15.
 */
public class KyLog {
  public static void LogFrame(ByteBuf byteBuf) {
    System.out.println(
        "begin:" + byteBuf.readerIndex() + ", end:" + byteBuf.writerIndex() + ", readable:" +
            byteBuf.readableBytes() + ", writable:" + byteBuf.writableBytes());
    System.out.println(ByteBufUtil.prettyHexDump(byteBuf));
  }
}
