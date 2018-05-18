package cc.bitky.clusterdeviceplatform.server.server.statistic.info;

import cc.bitky.clusterdeviceplatform.server.config.DbSetting;

public class DataBaseInfo {
    /**
     * 数据库类型
     */
    private String type;
    /**
     * MongoDB 的端口号
     */
    private int port;
    /**
     * MongoDB 的主机名
     */
    private String host;
    /**
     * MongoDB 的IP
     */
    private String ip;
    /**
     * 数据库
     */
    private String database;
    /**
     * 数据库鉴权用户名
     */
    private String databaseUsername;

    public DataBaseInfo() {
        this.type = "MongoDB";
        this.port = DbSetting.MONGODB_PORT;
        this.host = DbSetting.MONGODB_HOST;
        this.ip = DbSetting.MONGODB_IP;
        this.database = DbSetting.DATABASE;
        this.databaseUsername = DbSetting.DATABASE_USERNAME;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getType() {
        return type;
    }

    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }

    public String getIp() {
        return ip;
    }

    public String getDatabase() {
        return database;
    }
}
