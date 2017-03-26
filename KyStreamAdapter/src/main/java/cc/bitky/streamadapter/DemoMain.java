package cc.bitky.streamadapter;

import cc.bitky.streamadapter.util.KyLog;
import cc.bitky.streamadapter.util.bean.database.DoorInfo;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;
import java.util.ArrayList;
import java.util.List;

public class DemoMain {
  private static final String[] BYTE2HEX_PAD = new String[256];
  private static final String[] BYTE2HEX_NOPAD = new String[256];

  public static void main(String args[]) {
    System.out.println("hello lml");
    List<DoorInfo> doorInfos = new ArrayList<>(100);
    System.out.println(doorInfos.size());
    doorInfos.add(new DoorInfo());
    System.out.println(doorInfos.size());
    System.out.println(doorInfos.size());
    //    lml2();
    ////    DemoMain baseMessage = MessageBuilder.build();
    //    lml();
  }

  private static void lml2() {

    // Generate the lookup table that converts a byte into a 2-digit hexadecimal integer.
    int i;
    for (i = 0; i < 10; i++) {
      BYTE2HEX_PAD[i] = "0" + i;
      BYTE2HEX_NOPAD[i] = String.valueOf(i);
    }
    for (; i < 16; i++) {
      char c = (char) ('a' + i - 10);
      BYTE2HEX_PAD[i] = "0" + c;
      BYTE2HEX_NOPAD[i] = String.valueOf(c);
    }
    for (; i < BYTE2HEX_PAD.length; i++) {
      String str = Integer.toHexString(i);
      BYTE2HEX_PAD[i] = str;
      BYTE2HEX_NOPAD[i] = str;
    }
  }

  private static void lml() {
    ByteBuf byteBuf = Unpooled.buffer(1024);
    byte[] b = "李明亮".getBytes(CharsetUtil.UTF_8);
    KyLog.LogFrame(byteBuf);
    byteBuf.writeBytes(b);
    KyLog.LogFrame(byteBuf);
    byteBuf.readByte();
    KyLog.LogFrame(byteBuf);
    byteBuf.discardReadBytes();
    KyLog.LogFrame(byteBuf);
  }
}
