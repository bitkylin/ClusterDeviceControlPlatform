package cc.bitky.clustermanage.view.bean;

public class DeviceKey {
    private int groupId = -1;
    private int deviceId = -1;

    public DeviceKey(int groupId, int deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceKey deviceKey = (DeviceKey) o;

        if (groupId != deviceKey.groupId) return false;
        return deviceId == deviceKey.deviceId;
    }

    @Override
    public int hashCode() {
        int result = groupId;
        result = 31 * result + deviceId;
        return result;
    }
}
