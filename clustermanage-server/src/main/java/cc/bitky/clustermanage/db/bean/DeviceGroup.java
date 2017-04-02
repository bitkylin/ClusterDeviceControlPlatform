package cc.bitky.clustermanage.db.bean;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class DeviceGroup {

  @Id
  private String id;
  private long heartBeat;
  private int name;
  private int charging;
  private int full;
  private int using;
  private int problem;
  private List<Device> devices;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public long getHeartBeat() {
    return heartBeat;
  }

  public void setHeartBeat(long heartBeat) {
    this.heartBeat = heartBeat;
  }

  public int getName() {
    return name;
  }

  public void setName(int name) {
    this.name = name;
  }

  public int getCharging() {
    return charging;
  }

  public void setCharging(int charging) {
    this.charging = charging;
  }

  public int getFull() {
    return full;
  }

  public void setFull(int full) {
    this.full = full;
  }

  public int getUsing() {
    return using;
  }

  public void setUsing(int using) {
    this.using = using;
  }

  public int getProblem() {
    return problem;
  }

  public void setProblem(int problem) {
    this.problem = problem;
  }

  public List<Device> getDevices() {
    return devices;
  }

  public void setDevices(List<Device> devices) {
    this.devices = devices;
  }
}
