package cc.bitky.clustermanage.tcp.util.bean.database;

public class DoorInfo {
  private String MinerObjectId;
  private long StatusTime = -1;
  private int MinerLampStatus = -1;

  public String getMinerObjectId() {
    return MinerObjectId;
  }

  public void setMinerObjectId(String minerObjectId) {
    MinerObjectId = minerObjectId;
  }

  public long getStatusTime() {
    return StatusTime;
  }

  public void setStatusTime(long statusTime) {
    StatusTime = statusTime;
  }

  public int getMinerLampStatus() {
    return MinerLampStatus;
  }

  public void setMinerLampStatus(int minerLampStatus) {
    MinerLampStatus = minerLampStatus;
  }
}
