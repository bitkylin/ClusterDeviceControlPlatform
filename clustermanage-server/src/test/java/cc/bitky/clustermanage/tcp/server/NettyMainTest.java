package cc.bitky.clustermanage.tcp.server;

import cc.bitky.clustermanage.tcp.util.KyLog;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

public class NettyMainTest {
  @Test
  public void testMain() {
    byte a = (byte) 0x80;
    byte b = (byte) (a + 9);

    byte[] as = new byte[] {a, b};
    ByteBuf byteBuf = Unpooled.copiedBuffer(as);
    KyLog.LogFrame(byteBuf);
  }
}
