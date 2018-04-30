package cc.bitky.clusterdeviceplatform.server.web.spa.utils;

import org.slf4j.Logger;

public class WebUtil {
    public static void printTimeConsumed(long start, Logger logger) {
        long l2 = System.currentTimeMillis();
        logger.info("耗时：" + (l2 - start) + " ms");
    }
}
