package cc.bitky.clusterdeviceplatform.server.config;

public class ServerSetting {
    /**
     * 项目版本号
     */
    public static final String VERSION = "1.0.3";
    /**
     * 本地配置文件名
     */
    public static final String CONFIG_FILE_PATH = "setting";
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
    public static boolean WEB_RADOM_DEBUG = true;


}
