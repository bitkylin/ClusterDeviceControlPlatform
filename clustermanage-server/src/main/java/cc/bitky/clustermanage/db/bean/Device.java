package cc.bitky.clustermanage.db.bean;

public class Device {
  private String employeeObjectId;
  private long time = -1;
  private int status = -1;

  public String getEmployeeObjectId() {
    return employeeObjectId;
  }

  public void setEmployeeObjectId(String employeeObjectId) {
    this.employeeObjectId = employeeObjectId;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }
}
