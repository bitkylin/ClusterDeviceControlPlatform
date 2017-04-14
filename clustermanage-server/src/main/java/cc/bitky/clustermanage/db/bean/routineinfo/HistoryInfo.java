package cc.bitky.clustermanage.db.bean.routineinfo;

public class HistoryInfo {

    private long time = -1;
    private int status = -1;

    public HistoryInfo(long time, int status) {
        this.time = time;
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
