package cc.bitky.clustermanage.db.bean.routineinfo;

public class DutyInfo {

    private long onTime = -1;
    private long offTime = -1;

    public DutyInfo(long onTime) {
        this.onTime = onTime;
    }

    public DutyInfo() {
    }

    public long getOnTime() {
        return onTime;
    }

    public void setOnTime(long onTime) {
        this.onTime = onTime;
    }

    public long getOffTime() {
        return offTime;
    }

    public void setOffTime(long offTime) {
        this.offTime = offTime;
    }
}
