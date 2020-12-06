package cc.bitky.clusterdeviceplatform.server.db.statistic.status;

import lombok.Getter;

import java.util.List;

/**
 * TCP模块中单个 Channel 的当前负载量
 */
@Getter
public class DeviceGroupItem {
    /**
     * 单组的设备数量
     */
    private List<DeviceStatusItem> items;
    /**
     * 设备总数
     */
    private int deviceCount;
    /**
     * 使用中的设备数
     */
    private int usingCount;
    /**
     * 充电中中的设备数
     */
    private int chargingCount;
    /**
     * 已充满的设备数
     */
    private int fullCount;
    /**
     * 未初始化的设备数
     */
    private int uninitCount;
    /**
     * 设备组的 ID
     */
    private int id;
    /**
     * 待发送缓冲区中的消息数
     */
    private int msgCount;


    public DeviceGroupItem(int id, List<DeviceStatusItem> items, int msgCount, int deviceCount, int usingCount, int chargingCount, int fullCount, int uninitCount) {
        this.id = id;
        this.items = items;
        this.msgCount = msgCount;
        this.deviceCount = deviceCount;
        this.usingCount = usingCount;
        this.chargingCount = chargingCount;
        this.fullCount = fullCount;
        this.uninitCount = uninitCount;
    }
}
