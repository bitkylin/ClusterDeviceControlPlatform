package cc.bitky.clusterdeviceplatform.server.web.client.bean;


/**
 * CAN 帧发送队列局部信息
 */
public class QueueInfo {
    /**
     * CAN 帧发送队列中帧的数量
     */
    private int size;
    /**
     * CAN 帧发送队列容量
     */
    private int capacity;
    /**
     * CAN 帧发送间隔
     */
    private int interval;

    public QueueInfo(int size, int capacity, int interval) {
        this.size = size;
        this.capacity = capacity;
        this.interval = interval;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }
}
