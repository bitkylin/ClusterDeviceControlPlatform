package cc.bitky.clustermanage.db.bean.routineinfo;

public class DutyInfo {
  private long time = -1;
  private int status = -1;

  public DutyInfo(long time, int status) {
    this.time = time;
    this.status = status;
  }
}
