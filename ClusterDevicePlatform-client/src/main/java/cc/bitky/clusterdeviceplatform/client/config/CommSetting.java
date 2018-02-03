package cc.bitky.clusterdeviceplatform.client.config;

public class CommSetting {
    /**
     * 待发送缓冲队列中，帧发送间隔「单位/ms」
     */
    public static int FRAME_SEND_INTERVAL = 50;
    /**
     * 服务器主机名
     */
    public static String SERVER_HOSTNAME = "cdg-pc";
    /**
     * 服务器端口号
     */
    public static int SERVER_PORT = 30232;
    /**
     * TCP 通道验证时间
     */
    public static int ACCESSIBLE_CHANNEL_REPLY_INTERVAL = 1000;
}
