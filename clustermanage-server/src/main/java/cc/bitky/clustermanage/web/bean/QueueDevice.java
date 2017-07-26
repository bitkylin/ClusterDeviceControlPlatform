package cc.bitky.clustermanage.web.bean;

/**
 * 选择是否部署设备中的信息
 */
public class QueueDevice {

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

    public QueueDevice(boolean postName, boolean postDepartment, boolean postCardNumber, boolean postRemainChargeTime) {
        this.postName = postName;
        this.postDepartment = postDepartment;
        this.postCardNumber = postCardNumber;
        this.postRemainChargeTime = postRemainChargeTime;
    }


    public boolean isPostName() {
        return postName;
    }

    public void setPostName(boolean postName) {
        this.postName = postName;
    }

    public boolean isPostDepartment() {
        return postDepartment;
    }

    public void setPostDepartment(boolean postDepartment) {
        this.postDepartment = postDepartment;
    }

    public boolean isPostCardNumber() {
        return postCardNumber;
    }

    public void setPostCardNumber(boolean postCardNumber) {
        this.postCardNumber = postCardNumber;
    }

    public boolean isPostRemainChargeTime() {
        return postRemainChargeTime;
    }

    public void setPostRemainChargeTime(boolean postRemainChargeTime) {
        this.postRemainChargeTime = postRemainChargeTime;
    }
}
