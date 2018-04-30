package cc.bitky.clusterdeviceplatform.server.web.spa.tcp.bean;

import java.util.stream.Collectors;

import cc.bitky.clusterdeviceplatform.server.server.repo.bean.DeviceItem;

public class DeviceItemMsgSending extends BaseMsgSending {

    private final int groupId;
    private final int deviceId;

    private DeviceItemMsgSending(int groupId, int deviceId, int msgCount, int msgSendingCount) {
        super(msgCount, msgSendingCount);
        this.deviceId = deviceId;
        this.groupId = groupId;
    }

    private DeviceItemMsgSending(DeviceItem deviceItem) {
        this(deviceItem.getGroupId(), deviceItem.getDeviceId(), deviceItem.getMsgCount(), deviceItem.getMsgSendingCount());
    }

    public static DeviceItemMsgSending create(DeviceItem deviceItem, boolean needDetail, int msgLimit) {
        DeviceItemMsgSending itemMsgSending = new DeviceItemMsgSending(deviceItem);
        if (needDetail) {
            itemMsgSending.setMsgSendings(
                    deviceItem.getCacheMsg().stream()
                            .limit(msgLimit)
                            .map(MsgSending::new)
                            .collect(Collectors.toList()));
        }
        return itemMsgSending;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getDeviceId() {
        return deviceId;
    }
}
