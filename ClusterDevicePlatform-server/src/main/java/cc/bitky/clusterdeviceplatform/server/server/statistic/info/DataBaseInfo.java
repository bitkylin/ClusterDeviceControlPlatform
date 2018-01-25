package cc.bitky.clusterdeviceplatform.server.server.statistic.info;

import cc.bitky.clusterdeviceplatform.server.config.DbSetting;
import cc.bitky.clusterdeviceplatform.server.server.statistic.utils.IpUtil;

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
        this.ip = IpUtil.getIP(host)[0];
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
