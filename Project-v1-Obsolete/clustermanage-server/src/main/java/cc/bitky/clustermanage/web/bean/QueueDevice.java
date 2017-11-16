package cc.bitky.clustermanage.web.bean;

/**
 * 选择是否部署设备中的信息
 */
public class QueueDevice {
    /**
     * 设备组号
     */
    private int groupId;
    /**
     * 设备号
     */
    private int deviceId;
    /**
     * 部署姓名
     */
    private boolean postName;
    /**
     * 部署单位
     */
    private boolean postDepartment;
    /**
     * 部署卡号
     */
    private boolean postCardNumber;
    /**
     * 部署剩余充电次数
     */
    private boolean postRemainChargeTime;

    public QueueDevice(int groupId, int deviceId, boolean postName, boolean postDepartment, boolean postCardNumber, boolean postRemainChargeTime) {
        this.groupId = groupId;
        this.deviceId = deviceId;
        this.postName = postName;
        this.postDepartment = postDepartment;
        this.postCardNumber = postCardNumber;
        this.postRemainChargeTime = postRemainChargeTime;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public boolean isPostName() {
        return postName;
    }

    public boolean isPostDepartment() {
        return postDepartment;
    }

    public boolean isPostCardNumber() {
        return postCardNumber;
    }

    public boolean isPostRemainChargeTime() {
        return postRemainChargeTime;
    }
}
