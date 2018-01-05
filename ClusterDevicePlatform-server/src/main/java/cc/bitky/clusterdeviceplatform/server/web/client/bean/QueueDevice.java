package cc.bitky.clusterdeviceplatform.server.web.client.bean;

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
     * 部署设备启用状态
     */
    private boolean enabled;
    /**
     * 部署剩余充电次数
     */
    private boolean remainChargeTime;
    /**
     * 设备更新：重置剩余充电次数
     */
    private boolean replace;
    /**
     * 卡号集合的类型
     */
    private CardType cardSetType = CardType.None;
    public QueueDevice(int groupId, int deviceId, boolean name, boolean department, boolean card, boolean enabled, boolean remainChargeTime) {
        this(groupId, deviceId);
        this.name = name;
        this.department = department;
        this.card = card;
        this.enabled = enabled;
        this.remainChargeTime = remainChargeTime;
    }

    public QueueDevice(int groupId, int deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public CardType getCardSetType() {
        return cardSetType;
    }

    public void setCardSetType(CardType cardSetType) {
        this.cardSetType = cardSetType;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
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
