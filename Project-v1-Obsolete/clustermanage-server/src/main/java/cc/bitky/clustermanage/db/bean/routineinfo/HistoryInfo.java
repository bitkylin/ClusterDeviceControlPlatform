package cc.bitky.clustermanage.db.bean.routineinfo;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "HistoryInfo")
public class HistoryInfo {

    @Field("Time")
    private Date time;

    @Field("Status")
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
