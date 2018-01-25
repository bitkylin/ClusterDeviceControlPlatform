package cc.bitky.clusterdeviceplatform.server.server.utils;

import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;

public class DeviceOutBoundDetect {
    /**
     * 指定的组号和设备号是否超出系统设定的界限
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return 已超出界限返回true
     */
    public static boolean detect(int groupId, int deviceId) {
        return groupId > DeviceSetting.MAX_GROUP_ID || groupId <= 0 || deviceId > DeviceSetting.MAX_DEVICE_ID || deviceId <= 0;
    }

    /**
     * 指定的组号和设备号是否超出系统设定的界限
     *
     * @param deviceId 设备 ID
     * @return 是否超出界限
     */
    public static boolean detectDevice(int deviceId) {
        return deviceId > DeviceSetting.MAX_DEVICE_ID || deviceId <= 0;
    }

    /**
     * 指定的组号和设备号是否超出系统设定的界限
     *
     * @param groupId 设备组 ID
     * @return 是否超出界限
     */
    public static boolean detectGroup(int groupId) {
        return groupId > DeviceSetting.MAX_GROUP_ID || groupId <= 0;
    }
}
