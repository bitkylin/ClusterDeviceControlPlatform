package cc.bitky.clustermanage.tcp.util;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

public class KyLog {
  public static void LogFrame(ByteBuf byteBuf) {
    System.out.println(
        "begin:" + byteBuf.readerIndex() + ", end:" + byteBuf.writerIndex() + ", readable:" +
            byteBuf.readableBytes() + ", writable:" + byteBuf.writableBytes());
    System.out.println(ByteBufUtil.prettyHexDump(byteBuf));
  }
}
