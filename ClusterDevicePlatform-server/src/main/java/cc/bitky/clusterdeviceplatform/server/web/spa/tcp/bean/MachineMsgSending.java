package cc.bitky.clusterdeviceplatform.server.web.spa.tcp.bean;

import java.util.Collections;
import java.util.List;

public class MachineMsgSending extends BaseMsgSending {

    private List<DeviceGroupedMsgSending> deviceGroupedItems = Collections.emptyList();

    private MachineMsgSending(int msgCount, int msgSendingCount) {
        super(msgCount, msgSendingCount);
    }

    /**
     * 整体待发送消息统计，扁平化并展示“待发送消息对象”
     *
     * @param msgCount        消息总数
     * @param msgSendingCount 待发送消息总数
     * @param msgSendings     “待发送消息对象”集合
     * @return 设备组待发送消息统计
     */
    public static MachineMsgSending createFlat(int msgCount, int msgSendingCount, List<MsgSending> msgSendings) {
        MachineMsgSending itemGrouped = new MachineMsgSending(msgCount, msgSendingCount);
        itemGrouped.setMsgSendings(msgSendings);
        return itemGrouped;

    }

    /**
     * 整体待发送消息统计，对每个设备分别统计
     *
     * @param msgCount           消息总数
     * @param msgSendingCount    待发送消息总数
     * @param deviceGroupedItems “设备待发送消息统计”集合
     * @return 设备组待发送消息统计
     */
    public static MachineMsgSending createGather(int msgCount, int msgSendingCount, List<DeviceGroupedMsgSending> deviceGroupedItems) {
        MachineMsgSending itemGrouped = new MachineMsgSending(msgCount, msgSendingCount);
        itemGrouped.deviceGroupedItems = deviceGroupedItems;
        return itemGrouped;
    }

    /**
     * 整体待发送消息统计，无详细信息
     *
     * @param msgCount        消息总数
     * @param msgSendingCount 待发送消息总数
     * @return 设备组待发送消息统计
     */
    public static MachineMsgSending createOutline(int msgCount, int msgSendingCount) {
        return new MachineMsgSending(msgCount, msgSendingCount);
    }

    public List<DeviceGroupedMsgSending> getDeviceGroupedItems() {
        return deviceGroupedItems;
    }
}
