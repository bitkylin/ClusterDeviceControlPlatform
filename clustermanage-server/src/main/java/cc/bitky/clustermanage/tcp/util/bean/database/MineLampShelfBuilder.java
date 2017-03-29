package cc.bitky.clustermanage.tcp.util.bean.database;

import java.util.ArrayList;
import java.util.List;

public class MineLampShelfBuilder {
  private MineLampShelf mineLampShelf;

  public static MineLampShelfBuilder builder() {
    MineLampShelfBuilder shelfBuilder = new MineLampShelfBuilder();
    shelfBuilder.setMineLampShelf(new MineLampShelf());
    return shelfBuilder;
  }

  public MineLampShelfBuilder setId(int id) {
    mineLampShelf.setName(id);
    return this;
  }

  public MineLampShelf build() {
    mineLampShelf.setCharging(0);
    mineLampShelf.setFull(0);
    mineLampShelf.setUsing(0);
    mineLampShelf.setProblem(0);
    List<DoorInfo> doorInfos = new ArrayList<>(100);
    for (int i = 1; i <= 100; i++) {
      doorInfos.add(new DoorInfo());
    }
    mineLampShelf.setDoorInfos(doorInfos);
    return mineLampShelf;
  }

  public void setMineLampShelf(MineLampShelf mineLampShelf) {
    this.mineLampShelf = mineLampShelf;
  }
}
