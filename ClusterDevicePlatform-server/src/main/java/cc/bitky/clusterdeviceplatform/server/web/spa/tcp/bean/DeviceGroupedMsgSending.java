package cc.bitky.clusterdeviceplatform.server.web.spa.tcp.bean;

import java.util.Collections;
import java.util.List;

public class DeviceGroupedMsgSending extends BaseMsgSending {

    private final int groupId;
    private final long deviceCount;
    private List<DeviceItemMsgSending> deviceItems = Collections.emptyList();

    private DeviceGroupedMsgSending(int groupId, long deviceCount, int msgCount, int msgSendingCount) {
        super(msgCount, msgSendingCount);
        this.groupId = groupId;
        this.deviceCount = deviceCount;
    }

    /**
     * 设备组待发送消息统计，扁平化并展示“待发送消息对象”
     *
     * @param groupId         组号
     * @param deviceCount     发送消息的设备数量
     * @param msgCount        消息总数
     * @param msgSendingCount 待发送消息总数
     * @param msgSendings     “待发送消息对象”集合
     * @return 设备组待发送消息统计
     */
    public static DeviceGroupedMsgSending createFlat(int groupId, long deviceCount, int msgCount, int msgSendingCount, List<MsgSending> msgSendings) {
        DeviceGroupedMsgSending itemGrouped = new DeviceGroupedMsgSending(groupId, deviceCount, msgCount, msgSendingCount);
        itemGrouped.setMsgSendings(msgSendings);
        return itemGrouped;

    }

    /**
     * 设备组待发送消息统计，对每个设备分别统计
     *
     * @param groupId         组号
     * @param deviceCount     发送消息的设备数量
     * @param msgCount        消息总数
     * @param msgSendingCount 待发送消息总数
     * @param deviceItems     “设备待发送消息统计”集合
     * @return 设备组待发送消息统计
     */
    public static DeviceGroupedMsgSending createGather(int groupId, long deviceCount, int msgCount, int msgSendingCount, List<DeviceItemMsgSending> deviceItems) {
        DeviceGroupedMsgSending itemGrouped = new DeviceGroupedMsgSending(groupId, deviceCount, msgCount, msgSendingCount);
        itemGrouped.deviceItems = deviceItems;
        return itemGrouped;
    }

    /**
     * 设备组待发送消息统计，无详细信息
     *
     * @param groupId         组号
     * @param deviceCount     发送消息的设备数量
     * @param msgCount        消息总数
     * @param msgSendingCount 待发送消息总数
     * @return 设备组待发送消息统计
     */
    public static DeviceGroupedMsgSending createOutline(int groupId, long deviceCount, int msgCount, int msgSendingCount) {
        return new DeviceGroupedMsgSending(groupId, deviceCount, msgCount, msgSendingCount);
    }

    public long getDeviceCount() {
        return deviceCount;
    }

    public int getGroupId() {
        return groupId;
    }

    public List<DeviceItemMsgSending> getDeviceItems() {
        return deviceItems;
    }
}
