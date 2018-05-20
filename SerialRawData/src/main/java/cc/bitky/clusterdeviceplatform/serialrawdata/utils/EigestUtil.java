package cc.bitky.clusterdeviceplatform.serialrawdata.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 消息摘要算法工具类，此加密不可逆
 */
public class EigestUtil {

    private static MessageDigest messageDigest;

    static {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对字符串进行加扰
     *
     * @param str 原始字符串
     * @return 加扰后的字符串
     */
    private static String strConfuse(String str) {
        return DangerousKey.strConfuse(str);
    }

    /**
     * 对字符串进行摘要
     *
     * @param str 原始字符串
     * @return 摘要后的字符串
     */
    public static String strEigest(String str) {
        return StringConvert.byteArrayToHexStr(messageDigest.digest(strConfuse(str).getBytes()));
    }
}
