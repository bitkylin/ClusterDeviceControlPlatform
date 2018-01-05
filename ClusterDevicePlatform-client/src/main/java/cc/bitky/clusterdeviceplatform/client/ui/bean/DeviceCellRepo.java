package cc.bitky.clusterdeviceplatform.client.ui.bean;

import cc.bitky.clusterdeviceplatform.client.ui.view.DeviceCellView;

import static cc.bitky.clusterdeviceplatform.client.config.DeviceSetting.MAX_DEVICE_ID;
import static cc.bitky.clusterdeviceplatform.client.config.DeviceSetting.MAX_GROUP_ID;


/**
 * 设备及设备窗格缓存
 */
public class DeviceCellRepo {
    private static final DeviceCellView[] DEVICE_CELL_VIEWS = new DeviceCellView[MAX_GROUP_ID + 1];
    private static final Device[][] DEVICES = new Device[MAX_GROUP_ID + 1][MAX_DEVICE_ID + 1];

    static {
        for (int groupId = 0; groupId <= MAX_GROUP_ID; groupId++) {
            Device[] devices = DEVICES[groupId];
            for (int deviceId = 0; deviceId <= MAX_DEVICE_ID; deviceId++) {
                devices[deviceId] = new Device(groupId, deviceId);
            }
        }
    }

    public static Device getDevice(int groupId, int deviceId) {
        return DEVICES[groupId][deviceId];
    }

    public static void setDevice(Device device, int groupId, int deviceId) {
        DEVICES[groupId][deviceId] = device;
    }

    public static DeviceCellView getCellView(int id) {
        return DEVICE_CELL_VIEWS[id];
    }

    public static void setCellView(DeviceCellView deviceCellView, int id) {
        DEVICE_CELL_VIEWS[id] = deviceCellView;
    }
}
