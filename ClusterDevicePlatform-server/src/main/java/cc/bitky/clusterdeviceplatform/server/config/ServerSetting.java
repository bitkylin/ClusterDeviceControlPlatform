package cc.bitky.clusterdeviceplatform.server.config;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ServerSetting {
    /**
     * 系统开机时间
     */
    public static final LocalDateTime SYSTEM_START_DATE_TIME = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
    /**
     * 项目版本号
     */
    public static final String VERSION = "1.13.0";
    /**
     * 本地配置文件名
     */
    public static final String CONFIG_FILE_PATH = "setting";
    /**
     * 运行时环境
     */
    private static final RuntimeMXBean RUNTIME = ManagementFactory.getRuntimeMXBean();
    /**
     * PID
     */
    public static final String PID = RUNTIME.getName().split("@")[0];
    /**
     * 服务器TCP模块端口号
     */
    public static int SERVER_TCP_PORT = 30232;
    /**
     * 服务器Web模块端口号
     */
    public static int SERVER_WEB_PORT = 8080;
    /**
     * 是否开启调试信息输出
     */
    public static boolean DEBUG = false;
    /**
     * 是否开启web页面随机数据生成
     */
    public static boolean WEB_RANDOM_DEBUG = true;
}
