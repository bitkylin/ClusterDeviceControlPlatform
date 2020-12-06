package cc.bitky.clusterdeviceplatform.server.db.statistic.status;

import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.config.WorkStatus;
import lombok.Getter;

/**
 * TCP模块中单个 Channel 的当前负载量
 */
@Getter
public class DeviceStatusItem {
    /**
     * 通道的 ID
     */
    private int id;
    /**
     * 充电状态码
     */
    private int chargeStatus;
    /**
     * 充电状态文字描述
     */
    private String chargeStatusDescription;
    /**
     * 工作状态码
     */
    private int workStatus;
    /**
     * 工作状态文字描述
     */
    private String workStatusDescription;

    public DeviceStatusItem(int id, int chargeStatus, int workStatus) {
        this.id = id;
        this.chargeStatus = chargeStatus;
        this.chargeStatusDescription = ChargeStatus.obtainDescription(chargeStatus);
        this.workStatus = workStatus;
        this.workStatusDescription = WorkStatus.obtainDescription(workStatus);
    }
}
