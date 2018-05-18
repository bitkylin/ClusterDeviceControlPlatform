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
     * MongoDB 的IP地址
     */
    public static String MONGODB_IP = "未知";
    /**
     * 数据库
     */
    public static String DATABASE = "bitkyTest";
    /**
     * 异常消息的最大缓存容量
     */
    public static int FEEDBACK_ITEM_SIZE_MAX = 500;
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
    /**
     * 数据库认证用户名
     */
    public static String DATABASE_USERNAME = "数据库用户名";
    /**
     * 数据库认证密码
     */
    public static String DATABASE_PASSWORD = "数据库密码";
    /**
     * 数据库认证状态
     */
    public static boolean AUTHENTICATION_STATUS = false;
}
