package cc.bitky.clusterdeviceplatform.server.db.bean.routineinfo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "LampStatusHistory")
public class LampStatusHistory {

    @Id
    private String id;

    @Field("ChargeStatus")
    private List<HistoryInfo> chargeStatus = new ArrayList<>();

    @Field("WorkStatus")
    private List<HistoryInfo> workStatus = new ArrayList<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<HistoryInfo> getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(List<HistoryInfo> chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public List<HistoryInfo> getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(List<HistoryInfo> workStatus) {
        this.workStatus = workStatus;
    }
}
