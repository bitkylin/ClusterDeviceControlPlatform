package cc.bitky.clusterdeviceplatform.server.db.dto.routineinfo;

import java.util.Date;

public class DutyInfo {

    private Date onTime;
    private Date offTime;

    public DutyInfo(long onTime) {
        this.onTime = new Date(onTime);
    }

    public DutyInfo() {
    }

    public Date getOnTime() {
        return onTime;
    }

    public void setOnTime(Date onTime) {
        this.onTime = onTime;
    }

    public Date getOffTime() {
        return offTime;
    }

    public void setOffTime(Date offTime) {
        this.offTime = offTime;
    }
}
