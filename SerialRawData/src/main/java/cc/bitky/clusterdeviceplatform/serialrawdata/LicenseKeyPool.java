package cc.bitky.clusterdeviceplatform.serialrawdata;

import java.util.HashSet;
import java.util.List;

import cc.bitky.clusterdeviceplatform.serialrawdata.utils.EigestUtil;

/**
 * 验证池
 */
class LicenseKeyPool {

    private HashSet<String> pool = new HashSet<>();

    /**
     * 创建验证用授权码池
     *
     * @param list Mac 地址列表
     * @return 已创建的授权码池
     */
    static LicenseKeyPool createNetCardPool(List<NetCard> list) {
        if (list == null) {
            return null;
        }
        LicenseKeyPool licenseKeyPool = new LicenseKeyPool();
        list.forEach(item -> {
            if (item.getMac() != null && !"".equals(item.getMac().trim())) {
                licenseKeyPool.pool.add(EigestUtil.strEigest(item.getMac().trim()));
            }
        });
        return licenseKeyPool;
    }

    /**
     * 验证字符串是否在池中
     *
     * @param str 待验证的字符串
     * @return 是否在池中
     */
    private boolean verify(String str) {
        return pool.contains(str);
    }

    /**
     * 验证字符串组是否池中有其中之一
     *
     * @param strings 待验证的字符串组
     * @return 是否在池中
     */
    private boolean verify(String[] strings) {
        if (strings == null || strings.length == 0) {
            return false;
        }
        for (String string : strings) {
            if (string != null && !"".equals(string.trim()) && verify(string.trim())) {
                System.out.println("匹配 <" + string + ">");
                return true;
            }
        }
        return false;
    }


    /**
     * 在验证池中验证授权码
     *
     * @param str 待验证的授权码
     * @return 验证结果
     */
    boolean verifyLicenseKey(String str) {
        return verify(str.split("!"));
    }
}
