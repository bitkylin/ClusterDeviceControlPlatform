package cc.bitky.clusterdeviceplatform.server.db.statistic.status;

import lombok.Getter;

import java.util.List;

/**
 * 需统计的通道消息汇总
 */
@Getter
public class DeviceGroupOutline {
    /**
     * 设备组集合
     */
    private List<DeviceGroupItem> deviceGroupItems;
    /**
     * 设备组总数
     */
    private int deviceGroupCount;
    /**
     * 负载正常的界限
     */
    private int normalLimit;
    /**
     * 负载异常的界限
     */
    private int exceptionLimit;

    public DeviceGroupOutline(List<DeviceGroupItem> deviceGroupItems, int deviceGroupCount) {
        this.deviceGroupItems = deviceGroupItems;
        this.deviceGroupCount = deviceGroupCount;
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
}
