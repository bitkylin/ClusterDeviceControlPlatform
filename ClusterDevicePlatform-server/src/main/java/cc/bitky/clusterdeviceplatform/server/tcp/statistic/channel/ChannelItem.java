package cc.bitky.clusterdeviceplatform.server.tcp.statistic.channel;

/**
 * TCP模块中单个 Channel 的当前负载量
 */
public class ChannelItem {
    /**
     * 通道的 ID
     */
    private int id;
    /**
     * 当前Channel是否已启用
     */
    private boolean enabled;
    /**
     * 待发送缓冲区中的消息数
     */
    private int count;

    public ChannelItem(int id, boolean enabled, int count) {
        this.id = id;
        this.enabled = enabled;
        this.count = count;
    }


    public int getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getCount() {
        return count;
    }
}
