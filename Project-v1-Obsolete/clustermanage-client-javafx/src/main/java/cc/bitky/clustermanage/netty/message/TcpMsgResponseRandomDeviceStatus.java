package cc.bitky.clustermanage.netty.message;


/**
 * 设备回复: 随机充电状态
 */
public class TcpMsgResponseRandomDeviceStatus {

    private final int groupId;
    private final int statusMax;
    private final int length;

    public TcpMsgResponseRandomDeviceStatus(int groupId, int status, int length) {

        this.groupId = groupId;
        this.statusMax = status;
        this.length = length;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getStatusMax() {
        return statusMax;
    }

    public int getLength() {
        return length;
    }
}
