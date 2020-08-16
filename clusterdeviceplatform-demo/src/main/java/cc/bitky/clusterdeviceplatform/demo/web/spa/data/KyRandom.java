package cc.bitky.clusterdeviceplatform.demo.web.spa.data;

import cc.bitky.clusterdeviceplatform.demo.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.demo.db.statistic.status.DeviceGroupItem;
import cc.bitky.clusterdeviceplatform.demo.db.statistic.status.DeviceGroupOutline;
import cc.bitky.clusterdeviceplatform.demo.db.statistic.status.DeviceItem;
import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * @author limingliang
 */
public class KyRandom {

    private static final Random RANDOM = new Random();

    public static DeviceGroupOutline createOutline() {
        List<DeviceGroupItem> groupItems = new ArrayList<>(DeviceSetting.MAX_GROUP_ID);
        for (int groupId = 1; groupId <= DeviceSetting.MAX_GROUP_ID; groupId++) {
            groupItems.add(createSingleDeviceGroup(groupId, false));
        }
        return new DeviceGroupOutline(groupItems, DeviceSetting.MAX_GROUP_ID);
    }

    public static DeviceGroupOutline createDetail(int groupId) {
        List<DeviceGroupItem> groupItems = new ArrayList<>(1);
        groupItems.add(createSingleDeviceGroup(groupId, true));
        return new DeviceGroupOutline(groupItems, DeviceSetting.MAX_GROUP_ID);
    }

    private static DeviceGroupItem createSingleDeviceGroup(int groupId, boolean needDetail) {
        List<DeviceItem> deviceItems = new ArrayList<>(DeviceSetting.MAX_DEVICE_ID);
        int deviceCount = DeviceSetting.MAX_DEVICE_ID;
        int usingCount = 0;
        int chargingCount = 0;
        int fullCount = 0;
        int uninitCount = 0;
        int msgCount = RANDOM.nextInt(150);

        for (int deviceId = 1; deviceId <= DeviceSetting.MAX_DEVICE_ID; deviceId++) {
            int charge = RANDOM.nextInt(4);
            switch (charge) {
                case ChargeStatus.USING:
                    usingCount++;
                    break;
                case ChargeStatus.CHARGING:
                    chargingCount++;
                    break;
                case ChargeStatus.FULL:
                    fullCount++;
                    break;
                case ChargeStatus.UNINIT:
                    uninitCount++;
                    break;
                default:
                    break;
            }
            int work = RANDOM.nextInt(20);
            if (work < 4 || work > 10) {
                work = 1;
            }
            DeviceItem deviceItem = new DeviceItem(deviceId, charge, work);
            deviceItems.add(deviceItem);
        }
        if (!needDetail) {
            deviceItems = null;
        }
        return new DeviceGroupItem(groupId, deviceItems, msgCount, deviceCount, usingCount, chargingCount, fullCount, uninitCount);
    }
}
