package cc.bitky.clustermanage.db.bean.routineinfo;

import java.util.Date;

public class HistoryInfo {

    private Date time;
    private int status = -1;


    private HistoryInfo(long time, int status) {
        this.time = new Date(time);
        this.status = status;
    }

    public static HistoryInfo newInstance(long time, int status) {
        return new HistoryInfo(time, status);
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
