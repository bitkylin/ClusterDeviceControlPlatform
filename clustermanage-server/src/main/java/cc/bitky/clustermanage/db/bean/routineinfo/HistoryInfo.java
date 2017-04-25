package cc.bitky.clustermanage.db.bean.routineinfo;

import java.util.Date;

public class HistoryInfo {

    private Date time;
    private int status = -1;


    public HistoryInfo(long time, int status) {
        this.time = new Date(time);
        this.status = status;
    }

    public HistoryInfo() {
        time = new Date(0);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
