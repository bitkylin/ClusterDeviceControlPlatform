package cc.bitky.clusterdeviceplatform.demo.server.statistic.info;

import cc.bitky.clusterdeviceplatform.demo.config.DbSetting;

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

    public DataBaseInfo() {
        this.type = "MongoDB";
        this.port = DbSetting.MONGODB_PORT;
        this.host = DbSetting.MONGODB_HOST;
        this.ip = DbSetting.MONGODB_IP;
        this.database = DbSetting.DATABASE;
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
