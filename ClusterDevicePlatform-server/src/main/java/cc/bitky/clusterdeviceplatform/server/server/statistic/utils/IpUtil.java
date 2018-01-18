package cc.bitky.clusterdeviceplatform.server.server.statistic.utils;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class IpUtil {
    public static String getLocalHostName() {
        try {
            InetAddress addr = InetAddress.getLocalHost();
            return addr.getHostName();
        } catch (Exception ex) {
            return "未知";
        }
    }

    public static String[] getIP(String hostName) {
        List<String> strings = new ArrayList<>();
        try {
            if (hostName.length() > 0) {
                InetAddress[] addrs = InetAddress.getAllByName(hostName);
                if (addrs.length > 0) {
                    for (InetAddress address : addrs) {
                        String ip = address.getHostAddress();
                        if (ip.length() < 20) {
                            strings.add(ip);
                        }
                    }
                }
            }
        } catch (Exception ignored) {

        }

        if (strings.size() == 0) {
            strings.add("未知");
        }

        String[] ret = new String[strings.size()];
        strings.toArray(ret);
        return ret;
    }
}
