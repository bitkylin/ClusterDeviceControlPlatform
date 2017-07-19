package cc.bitky.clustermanage.view;

import java.util.HashMap;

import cc.bitky.clustermanage.view.bean.Device;
import cc.bitky.clustermanage.view.bean.DeviceKey;
import cc.bitky.clustermanage.view.viewbean.DeviceView;

public class Container {

    static final HashMap<Integer, DeviceView> deviceViewHashMap = new HashMap<>(100);

    public static final HashMap<DeviceKey, Device> deviceHashMap = new HashMap<>(10000);
}
