package cc.bitky.clustermanage.tcp;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KyTest {
    @Test
    public void test() {
        Date date = new Date();
        date.toString();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(date));
    }
//    @Test
//    public void testMain() {
//        byte a = (byte) 0x80;
//        byte b = (byte) (a + 1);
//        System.out.println(a);
//        System.out.println(b);
//        // a=b;
//        //System.out.println(a);
//        ByteBuf byteBuf = Unpooled.copiedBuffer(new byte[]{a, b});
//        KyLog.LogFrame(byteBuf);
//    }
//
//    @Test
//    public void charSetTest() {
//
//        String lml = "李";
//        ByteBuf byteBuf = Unpooled.copiedBuffer(lml.getBytes(Charset.forName("EUC-CN")));
//        KyLog.LogFrame(byteBuf);
//    }
//
//    @Test
//    public void 取模() {
//        int a = 9;
//        int b = a % 8;
//        int c = a % 7;
//        System.out.println(b);
//        System.out.println(c);
//    }
}
