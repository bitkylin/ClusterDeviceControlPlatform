package cc.bitky.clusterdeviceplatform.server.web.bean;

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
    private boolean name;
    /**
     * 部署单位
     */
    private boolean department;
    /**
     * 部署卡号
     */
    private boolean card;
    /**
     * 部署剩余充电次数
     */
    private boolean remainChargeTime;
    /**
     * 部署解锁指令
     */
    private boolean unLock;
    /**
     * 部署设备初始化指令
     */
    private boolean initialize;
    /**
     * 设备更新：重置剩余充电次数
     */
    private boolean renew;
    /**
     * 卡号集合的类型
     */
    private CardType cardSetType = CardType.None;

    public QueueDevice(int groupId, int deviceId, boolean name, boolean department, boolean card, boolean remainChargeTime) {
        this(groupId, deviceId);
        this.name = name;
        this.department = department;
        this.card = card;
        this.remainChargeTime = remainChargeTime;
    }

    public QueueDevice(int groupId, int deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    public CardType getCardSetType() {
        return cardSetType;
    }

    public void setCardSetType(CardType cardSetType) {
        this.cardSetType = cardSetType;
    }

    public boolean isRenew() {
        return renew;
    }

    public void setRenew(boolean renew) {
        this.renew = renew;
    }

    public boolean isInitialize() {
        return initialize;
    }

    public void setInitialize(boolean initialize) {
        this.initialize = initialize;
    }

    public boolean isUnLock() {
        return unLock;
    }

    public void setUnLock(boolean unLock) {
        this.unLock = unLock;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public boolean isName() {
        return name;
    }

    public boolean isDepartment() {
        return department;
    }

    public boolean isCard() {
        return card;
    }

    public boolean isRemainChargeTime() {
        return remainChargeTime;
    }
}
