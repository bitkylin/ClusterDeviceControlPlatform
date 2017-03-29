package cc.bitky.clustermanage.tcp.util.bean.database;

import java.util.List;

public class MineLampShelf {
  private long heartBeat;
  private int name;
  private int charging;
  private int full;
  private int using;
  private int problem;
  private List<DoorInfo> doorInfos;

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

  public List<DoorInfo> getDoorInfos() {
    return doorInfos;
  }

  public void setDoorInfos(List<DoorInfo> doorInfos) {
    this.doorInfos = doorInfos;
  }
}
