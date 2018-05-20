package cc.bitky.clusterdeviceplatform.serialrawdata.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 字符串加密解密工具，可逆加密，秘钥很重要，一定要自己改秘钥，打死也不要告诉其他人
 *
 * @author 夏增明
 * @version 1.0 Date:2013年2月8日15:45:56
 */
public class DesUtil {

    /**
     * 密钥，是加密解密的凭据，长度为8的倍数
     */
    private static final String PASSWORD_CRYPT_KEY = DangerousKey.getDesKey();
    private final static String DES = "DES";

    /**
     * 加密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return 返回加密后的数据
     */
    private static byte[] encrypt(byte[] src, byte[] key) throws Exception {

        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

        SecretKey secretKey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, sr);

        // 现在，获取数据并加密
        // 正式执行加密操作
        return cipher.doFinal(src);

    }

    /**
     * 解密
     *
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return 返回解密后的原始数据
     */
    private static byte[] decrypt(byte[] src, byte[] key) throws Exception {

        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);

        SecretKey secretKey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, secretKey, sr);

        // 现在，获取数据并解密
        // 正式执行解密操作
        return cipher.doFinal(src);

    }

    /**
     * 密码解密
     */
    public final static String decrypt(String data) {

        try {
            return new String(decrypt(hex2byte(data.getBytes()), PASSWORD_CRYPT_KEY.getBytes()));
        } catch (Exception ignored) {

        }
        return null;
    }

    /**
     * 密码加密
     */
    public final static String encrypt(String password) {
        try {
            return byte2hex(encrypt(password.getBytes(), PASSWORD_CRYPT_KEY.getBytes()));
        } catch (Exception ignored) {

        }
        return null;
    }

    /**
     * 二行制转字符串
     */
    private static String byte2hex(byte[] b) {

        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0").append(stmp);
            } else {
                hs.append(stmp);
            }
        }
        return hs.toString().toUpperCase();
    }

    private static byte[] hex2byte(byte[] b) {

        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 测试用例，不需要传递任何参数，直接执行即可。
     */
    public static void main(String[] args) {
        String basestr = "随机测试-bitkylin";
        String str1 = encrypt(basestr);

        System.out.println("原始值: " + basestr);
        System.out.println("加密后: " + str1);
        System.out.println("解密后: " + decrypt(str1));
        System.out.println("为空时 is : " + decrypt(encrypt("")));
    }
}