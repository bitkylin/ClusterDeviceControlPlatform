package cc.bitky.clusterdeviceplatform.server.server.repo.bean;


import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;

/**
 * 用于服务器临时存储的状态时间对象
 */
public class StatusItem {

    private final long time;

    private final int status;

    private StatusItem(long time, int status) {
        this.time = time;
        this.status = status;
    }

    public StatusItem() {
        this(-1, -1);
    }

    public static StatusItem newInstance(MsgReplyDeviceStatus item) {
        return new StatusItem(item.getTime(), item.getStatus());
    }

    public long getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }
}
