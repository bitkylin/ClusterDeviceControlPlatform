package cc.bitky.clustermanage.db;

import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.bean.DeviceGroup;
import java.util.ArrayList;
import java.util.List;

public class DeviceGroupBuilder {
  private DeviceGroup deviceGroup;

  public static DeviceGroupBuilder builder() {
    DeviceGroupBuilder shelfBuilder = new DeviceGroupBuilder();
    shelfBuilder.setDeviceGroup(new DeviceGroup());
    return shelfBuilder;
  }

  public DeviceGroupBuilder setId(int id) {
    deviceGroup.setName(id);
    return this;
  }

  public DeviceGroup build() {
    deviceGroup.setCharging(0);
    deviceGroup.setFull(0);
    deviceGroup.setUsing(0);
    deviceGroup.setProblem(0);
    List<Device> devices = new ArrayList<>(100);
    for (int i = 1; i <= 100; i++) {
      devices.add(new Device());
    }
    deviceGroup.setDevices(devices);
    return deviceGroup;
  }

  public void setDeviceGroup(DeviceGroup deviceGroup) {
    this.deviceGroup = deviceGroup;
  }
}
