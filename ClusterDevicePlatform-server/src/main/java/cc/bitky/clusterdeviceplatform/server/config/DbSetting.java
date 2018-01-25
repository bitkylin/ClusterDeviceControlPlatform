package cc.bitky.clusterdeviceplatform.server.config;

public class DbSetting {
    /**
     * MongoDB 的主机名
     */
    public static String MONGODB_HOST = "lml-desktop";
    /**
     * MongoDB 的端口号
     */
    public static int MONGODB_PORT = 27017;
    /**
     * 数据库
     */
    public static String DATABASE = "bitkyTest";
    /**
     * 异常消息的最大缓存容量
     */
    public static int FEEDBACK_ITEM_SIZE_MAX = 500;
    /**
     * 已连接通道未响应持续的时间间隔「ms」
     */
    public static int NO_RESPONSE_INTERVAL = 60 * 2 * 1000;

    /**
     * 默认员工卡号
     */
    public static String DEFAULT_EMPLOYEE_CARD_NUMBER = "0";
    /**
     * 默认员工姓名
     */
    public static String DEFAULT_EMPLOYEE_NAME = "备用";
    /**
     * 默认员工单位
     */
    public static String DEFAULT_EMPLOYEE_DEPARTMENT = "默认单位";
}
