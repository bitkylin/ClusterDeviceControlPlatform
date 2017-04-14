package cc.bitky.clustermanage.db.bean.routineinfo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class RoutineTables {

    @Id
    String id;
    List<DutyInfo> dutyInfos = new ArrayList<>();
    List<HistoryInfo> historyInfos = new ArrayList<>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<DutyInfo> getDutyInfos() {
        return dutyInfos;
    }

    public void setDutyInfos(List<DutyInfo> dutyInfos) {
        this.dutyInfos = dutyInfos;
    }

    public List<HistoryInfo> getHistoryInfos() {
        return historyInfos;
    }

    public void setHistoryInfos(List<HistoryInfo> historyInfos) {
        this.historyInfos = historyInfos;
    }
}
