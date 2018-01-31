package cc.bitky.clusterdeviceplatform.demo.server.statistic.info;


import cc.bitky.clusterdeviceplatform.demo.config.ServerSetting;
import cc.bitky.clusterdeviceplatform.demo.server.statistic.utils.IpUtil;

public class ServerInfo {
    /**
     * 服务器版本
     */
    String version = ServerSetting.VERSION;
    /**
     * web端口号
     */
    int webPort = ServerSetting.SERVER_WEB_PORT;
    /**
     * tcp端口号
     */
    int tcpPort= ServerSetting.SERVER_TCP_PORT;
    /**
     * 服务器主机名
     */
    String hostName;
    /**
     * 服务器地址
     */
    String[] ipAddress;

    public ServerInfo() {
        this.hostName = IpUtil.getLocalHostName();
        this.ipAddress = IpUtil.getIP(hostName);
    }

    public int getWebPort() {
        return webPort;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    public String getHostName() {
        return hostName;
    }

    public String[] getIpAddress() {
        return ipAddress;
    }

    public String getVersion() {
        return version;
    }
}
