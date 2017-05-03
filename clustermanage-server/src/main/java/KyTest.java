public class KyTest {

    public static void main(String[] args) {
        byte[] bytes = new byte[]{0x01, 0x00, 0x00, 0x01, 0x03, 0x65, (byte) 0x89, (byte) 0xA1};

        System.out.println(byteArrayToLong(bytes));
    }

    private static long byteArrayToLong(byte[] bytes) {
        long num = 0;
        for (int i = 0; i <= 7; i++) {
            num += (bytes[i] & 0xffL) << ((7 - i) * 8);
        }
        return num;
    }
}
