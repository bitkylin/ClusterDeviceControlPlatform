package cc.bitky.clusterdeviceplatform.server.tcp.statistic;

import java.util.List;

/**
 * 需统计的通道消息汇总
 */
public class ChannelOutline {
    /**
     * 通道对象集合
     */
    private List<ChannelItem> channelItems;
    /**
     * 已激活的通道总数
     */
    private int activatedCount;
    /**
     * 已激活的通道总数
     */
    private int inactivatedCount;
    /**
     * 等待激活的通道总数
     */
    private int waitToActivateCount;
    /**
     * 负载正常的界限
     */
    private int normalLimit;
    /**
     * 负载异常的界限
     */
    private int exceptionLimit;

    public ChannelOutline(List<ChannelItem> channelItems, int activatedCount, int inactivatedCount, int waitToActivateCount) {
        this.channelItems = channelItems;
        this.activatedCount = activatedCount;
        this.inactivatedCount = inactivatedCount;
        this.waitToActivateCount = waitToActivateCount;
    }

    public int getNormalLimit() {
        return normalLimit;
    }

    public int getExceptionLimit() {
        return exceptionLimit;
    }

    /**
     * 设置前端界面报警的限定
     *
     * @param normalLimit    负载正常的界限
     * @param exceptionLimit 负载异常的界限
     */
    public void setAlarmLimit(int normalLimit, int exceptionLimit) {
        this.normalLimit = normalLimit;
        this.exceptionLimit = exceptionLimit;
    }

    public List<ChannelItem> getChannelItems() {
        return channelItems;
    }

    public int getActivatedCount() {
        return activatedCount;
    }

    public int getInactivatedCount() {
        return inactivatedCount;
    }

    public int getWaitToActivateCount() {
        return waitToActivateCount;
    }
}
