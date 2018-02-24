package cc.bitky.clusterdeviceplatform.server.server.repo;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cc.bitky.clusterdeviceplatform.messageutils.config.WorkStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.controlcenter.MsgCodecHeartbeat;
import cc.bitky.clusterdeviceplatform.server.config.DbSetting;
import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.except.TcpFeedbackItem;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.except.TypeEnum;

/**
 * 用于TCP反馈消息对象的缓存容器
 */
@Repository
public class TcpFeedBackRepository {
    private final LinkedBlockingDeque<TcpFeedbackItem> feedbackItems = new LinkedBlockingDeque<>();
    /**
     * 设备运行状态正常「无未响应，无断开，无重发」
     */
    private final AtomicBoolean[] channelNormalList = new AtomicBoolean[DeviceSetting.MAX_GROUP_ID + 1];

    {
        for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
            channelNormalList[i] = new AtomicBoolean(true);
        }
    }

    /**
     * 向队列中添加该项，若已存在该项，则覆盖
     *
     * @param item 欲添加的项目
     */
    public void putItem(TcpFeedbackItem item) {
        feedbackItems.remove(item);
        feedbackItems.offer(item);
        if (feedbackItems.size() > DbSetting.FEEDBACK_ITEM_SIZE_MAX) {
            feedbackItems.poll();
        }

        if (item.getType() == TypeEnum.RESEND_OUT_BOUND ||
                item.getType() == TypeEnum.CHANNEL_DISCONNECT ||
                item.getType() == TypeEnum.DEVICE_GROUP_NO_RESPONSE) {
            channelNormalList[item.getGroupId()].set(false);
        }
    }

    public void removeItem(TcpFeedbackItem item) {
        feedbackItems.remove(item);
    }

    /**
     * 根据条件对List中的Item进行过滤，返回状态异常消息或超时消息
     *
     * @param type 请求类型
     * @return 指定的返回的消息集合
     */
    public List<TcpFeedbackItem> getItems(ItemType type) {
        return getTcpFeedbackItemsStream(type).collect(Collectors.toList());
    }

    /**
     * 根据条件对List中的Item进行过滤，返回状态异常消息或超时消息的数量
     *
     * @param type 请求类型
     * @return 指定的返回的消息的数量
     */
    public long getItemsCount(ItemType type) {
        return getTcpFeedbackItemsStream(type).count();
    }

    private Stream<TcpFeedbackItem> getTcpFeedbackItemsStream(ItemType type) {
        switch (type) {
            case EXCEPTION:
                return feedbackItems.stream().filter(item -> {
                    if (item.getType() == TypeEnum.WORK_STATUS_EXCEPTION) {
                        MsgReplyDeviceStatus deviceStatus = (MsgReplyDeviceStatus) item.getBaseMsg();
                        if (deviceStatus.getType() == MsgReplyDeviceStatus.Type.WORK) {
                            return deviceStatus.getStatus() != WorkStatus.WORK_TIME_OVER && deviceStatus.getStatus() != WorkStatus.CHARGING_TIME_OVER;
                        }
                    }
                    return true;
                });
            case TIMEOUT:
                return feedbackItems.stream().filter(item -> {
                    if (item.getType() == TypeEnum.WORK_STATUS_EXCEPTION) {
                        MsgReplyDeviceStatus deviceStatus = (MsgReplyDeviceStatus) item.getBaseMsg();
                        if (deviceStatus.getType() == MsgReplyDeviceStatus.Type.WORK) {
                            return deviceStatus.getStatus() == WorkStatus.WORK_TIME_OVER || deviceStatus.getStatus() == WorkStatus.CHARGING_TIME_OVER;
                        }
                    }
                    return false;
                });
            default:
                return Stream.empty();
        }
    }

    public void clearItems() {
        feedbackItems.clear();
    }

    /**
     * 将设备的异常状态去除，恢复为正常状态
     *
     * @param groupId 设备组 ID
     */
    public void recoveryItem(int groupId) {
        if (!channelNormalList[groupId].get()) {
            removeItem(TcpFeedbackItem.createChannelDisconnect(groupId));
            removeItem(TcpFeedbackItem.createChannelNoResponse(groupId));
            removeItem(TcpFeedbackItem.createResendOutBound(MsgCodecHeartbeat.create(groupId)));
            channelNormalList[groupId].set(true);
        }
    }

    /**
     * 消息类型
     */
    public enum ItemType {
        /**
         * 异常消息对象
         */
        EXCEPTION,
        /**
         * 超时消息对象
         */
        TIMEOUT
    }
}
