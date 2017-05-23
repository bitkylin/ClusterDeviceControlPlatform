package cc.bitky.clustermanage.web.bean;


public class QueueInfo {
    private int size;
    private int capacity;
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
