package cc.bitky.clusterdeviceplatform.server.config;

public class DbSetting {
    /**
     * MongoDB 的主机名
     */
    public static String mongodbHost = "lml-desktop";
    /**
     * MongoDB 的端口号
     */
    public static int mongodbPort = 27017;
    /**
     * MongoDB 的IP地址
     */
    public static String mongodbIp = "未知";
    /**
     * 数据库
     */
    public static String database = "bitkyTest";
    /**
     * 异常消息的最大缓存容量
     */
    public static int feedbackItemSizeMax = 500;
    /**
     * 默认员工卡号
     */
    public static String defaultEmployeeCardNumber = "0";
    /**
     * 默认员工姓名
     */
    public static String defaultEmployeeName = "备用";
    /**
     * 默认员工单位
     */
    public static String defaultEmployeeDepartment = "默认单位";
    /**
     * 数据库认证用户名
     */
    public static String databaseUsername = "数据库用户名";
    /**
     * 数据库认证密码
     */
    public static String databasePassword = "数据库密码";
    /**
     * 数据库认证状态
     */
    public static boolean databaseAuthenticationStatus = false;
}
