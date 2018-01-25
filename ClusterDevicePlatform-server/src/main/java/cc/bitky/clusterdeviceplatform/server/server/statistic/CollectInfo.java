package cc.bitky.clusterdeviceplatform.server.server.statistic;

import cc.bitky.clusterdeviceplatform.server.server.ServerCenterProcessor;
import cc.bitky.clusterdeviceplatform.server.server.statistic.info.DataBaseInfo;
import cc.bitky.clusterdeviceplatform.server.server.statistic.info.ServerInfo;
import cc.bitky.clusterdeviceplatform.server.server.statistic.info.ServerSettingInfo;
import cc.bitky.clusterdeviceplatform.server.server.statistic.info.ServerStatusInfo;
import cc.bitky.clusterdeviceplatform.server.server.statistic.info.SysEnvInfo;
import cc.bitky.clusterdeviceplatform.server.server.statistic.info.TcpDetailInfo;
import cc.bitky.clusterdeviceplatform.server.server.statistic.info.TcpInfo;

public class CollectInfo {
    private ServerInfo serverInfo;
    private ServerSettingInfo serverSettingInfo;
    private ServerStatusInfo serverStatusInfo;
    private SysEnvInfo sysEnvInfo;
    private DataBaseInfo dataBaseInfo;
    private TcpInfo tcpInfo;
    private TcpDetailInfo tcpDetailInfo;

    public CollectInfo(ServerCenterProcessor serverProcessor) {
        this.serverInfo = new ServerInfo();
        this.serverSettingInfo = new ServerSettingInfo();
        this.serverStatusInfo = new ServerStatusInfo(serverProcessor);
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
