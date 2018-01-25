package cc.bitky.clusterdeviceplatform.server.server.statistic.info;

import java.util.Properties;

/**
 * 系统运行环境
 */
public class SysEnvInfo {
    /**
     * jvm
     */
    String jvm;
    /**
     * 版本
     */
    String version;
    /**
     * 操作系统
     */
    String system;
    /**
     * 架构
     */
    String framework;
    /**
     * 语言
     */
    String language;
    /**
     * 用户名
     */
    String userName;
    /**
     * 文件编码
     */
    String fileEncoding;
    /**
     * 系统编码
     */
    String sysEncoding;

    public SysEnvInfo() {
        Properties props = System.getProperties();
        this.jvm = props.getProperty("java.vm.name");
        this.version = props.getProperty("java.runtime.version");
        this.system = props.getProperty("os.name");
        this.framework = props.getProperty("os.arch");
        this.language = props.getProperty("user.language");
        this.userName = props.getProperty("user.name");
        this.fileEncoding = props.getProperty("file.encoding");
        this.sysEncoding = props.getProperty("sun.jnu.encoding");
    }

    public String getJvm() {
        return jvm;
    }

    public String getVersion() {
        return version;
    }

    public String getSystem() {
        return system;
    }

    public String getFramework() {
        return framework;
    }

    public String getLanguage() {
        return language;
    }

    public String getUserName() {
        return userName;
    }

    public String getFileEncoding() {
        return fileEncoding;
    }

    public String getSysEncoding() {
        return sysEncoding;
    }
}
