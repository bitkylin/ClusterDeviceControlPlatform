package cc.bitky.clusterdeviceplatform.server.config;

/**
 * 本地配置文件对象
 */
public class LocalProfile {
    /**
     * 已连接设备组未响应持续的时间间隔
     */
    public int 通道未响应时间 = 2 * 60;
    /**
     * 已连接设备组指定时间无响应检测
     */
    public boolean 通道无响应监测 = true;
    /**
     * 监测帧是否成功送达客户端，未送达即重发
     */
    public boolean 帧送达监测 = true;
    /**
     * 是否开启调试信息输出
     */
    public boolean 调试模式 = false;
    /**
     * 是否开启web调用返回随机数据
     */
    public boolean 随机Web数据模式 = false;
    /**
     * 部署员工信息时，若数据库中无指定的相关信息，而部署的默认员工默认信息
     */
    public String 员工默认卡号 = "";

    public String 员工默认姓名 = "备用";

    public String 员工默认部门 = "默认单位";

    /**
     * 当剩余充电次数小于该阈值时，服务器下发剩余充电次数
     */
    public int 部署剩余充电次数阈值 = 20;
    /**
     * 服务器下发数据帧时，帧发送间隔「单位：ms」
     */
    public int 帧发送间隔 = 60;
    /**
     * 检错重发最大次数，服务器向 TCP 通道发送 CAN 帧，最大重复发送次数
     */
    public int 检错重发最大重复次数 = 5;
//    /**
//     * 新矿灯的初始化充电次数
//     */
//    public int 初始充电次数 = 500;
    /**
     * 服务器 TCP 客户端的端口号
     */
    public int 服务器端口号 = 30232;
    /**
     * 数据库服务器的主机名或者IP地址
     */
    public String 数据库服务器的主机名或IP = "localhost";
}
