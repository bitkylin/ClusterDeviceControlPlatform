package cc.bitky.clustermanage.db.bean;

import cc.bitky.clustermanage.db.bean.routineinfo.DutyInfo;
import cc.bitky.clustermanage.db.bean.routineinfo.HistoryInfo;
import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class RoutineInfo {
  @Id
  String id;

  List<DutyInfo> dutyInfos;
  List<HistoryInfo> historyInfos;
}
