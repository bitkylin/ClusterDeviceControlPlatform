package cc.bitky.clusterdeviceplatform.server.pojo.dataprocess;

public class DeviceStatusItem {
    /**
     * 充电状态码
     */
    private int chargeStatus;
    /**
     * 工作状态码
     */
    private int workStatus;

    public DeviceStatusItem(int chargeStatus, int workStatus) {
        this.chargeStatus = chargeStatus;
        this.workStatus = workStatus;
    }

    public int getChargeStatus() {
        return chargeStatus;
    }

    public int getWorkStatus() {
        return workStatus;
    }
}
