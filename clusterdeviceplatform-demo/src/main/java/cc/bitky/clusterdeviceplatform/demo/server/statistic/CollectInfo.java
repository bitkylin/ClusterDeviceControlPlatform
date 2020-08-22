package cc.bitky.clusterdeviceplatform.demo.server.statistic;


import cc.bitky.clusterdeviceplatform.demo.server.statistic.info.*;

public class CollectInfo {

    private final ServerInfo serverInfo;
    private final ServerSettingInfo serverSettingInfo;
    private final ServerStatusInfo serverStatusInfo;
    private final SysEnvInfo sysEnvInfo;
    private final DataBaseInfo dataBaseInfo;
    private final TcpInfo tcpInfo;
    private final TcpDetailInfo tcpDetailInfo;

    public CollectInfo() {
        this.serverInfo = new ServerInfo();
        this.serverSettingInfo = new ServerSettingInfo();
        this.serverStatusInfo = new ServerStatusInfo();
        this.sysEnvInfo = new SysEnvInfo();
        this.dataBaseInfo = new DataBaseInfo();
        this.tcpInfo = new TcpInfo();
        this.tcpDetailInfo = new TcpDetailInfo();
    }

    public ServerStatusInfo getServerStatusInfo() {
        return serverStatusInfo;
    }

    public DataBaseInfo getDataBaseInfo() {
        return dataBaseInfo;
    }

    public ServerInfo getServerInfo() {
        return serverInfo;
    }

    public ServerSettingInfo getServerSettingInfo() {
        return serverSettingInfo;
    }

    public SysEnvInfo getSysEnvInfo() {
        return sysEnvInfo;
    }

    public TcpInfo getTcpInfo() {
        return tcpInfo;
    }

    public TcpDetailInfo getTcpDetailInfo() {
        return tcpDetailInfo;
    }
}
