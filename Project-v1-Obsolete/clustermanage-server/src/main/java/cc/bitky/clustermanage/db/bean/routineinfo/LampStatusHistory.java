package cc.bitky.clustermanage.db.bean.routineinfo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "LampStatusHistory")
public class LampStatusHistory {

    @Id
    private String id;

    @Field("StatusList")
    private List<HistoryInfo> statusList = new ArrayList<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<HistoryInfo> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<HistoryInfo> statusList) {
        this.statusList = statusList;
    }
}
