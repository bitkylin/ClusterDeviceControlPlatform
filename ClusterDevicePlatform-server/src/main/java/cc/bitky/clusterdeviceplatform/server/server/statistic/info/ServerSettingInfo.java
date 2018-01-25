package cc.bitky.clusterdeviceplatform.server.server.statistic.info;

import cc.bitky.clusterdeviceplatform.server.config.CommSetting;
import cc.bitky.clusterdeviceplatform.server.config.ServerSetting;

public class ServerSettingInfo {
    /**
     * 是否开启调试模式
     */
    boolean debugMode;
    /**
     * 是否开启检错重发
     */
    boolean needReplyMode;
    /**
     * Web页面数据随机生成
     */
    boolean randomDataMode;
    /**
     * 本地配置文件名
     */
    String configFilePath;
    public ServerSettingInfo() {
        this.debugMode = ServerSetting.DEBUG;
        this.needReplyMode = CommSetting.DEPLOY_MSG_NEED_REPLY;
        this.randomDataMode = ServerSetting.WEB_RANDOM_DEBUG;
        this.configFilePath = ServerSetting.CONFIG_FILE_PATH;
    }

    public boolean isNeedReplyMode() {
        return needReplyMode;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public boolean isRandomDataMode() {
        return randomDataMode;
    }

    public String getConfigFilePath() {
        return configFilePath;
    }
}
