package cc.bitky.clusterdeviceplatform.server.tcp.statistic.except;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;

/**
 * 「信息统计」TCP 反馈消息对象
 */
public class TcpFeedbackItem {
    /**
     * 当前日期
     */
    private String currentDate;
    /**
     * 当前时间
     */
    private String currentTime;
    /**
     * 当前时间戳
     */
    private long milliseconds;
    private int groupId;
    private int deviceId;
    private TypeEnum type;
    private String description;
    private BaseMsg baseMsg;
    private Instant instant;

    private TcpFeedbackItem(int groupId, int deviceId, TypeEnum type, BaseMsg baseMsg) {
        this.groupId = groupId;
        this.deviceId = deviceId;
        this.type = type;
        this.description = type.getDetail();
        this.baseMsg = baseMsg;
        calculateTime();
    }

    /**
     * 创建「通道断开」反馈消息对象
     *
     * @param channelId 通道 ID
     * @return 已创建的反馈消息对象
     */
    public static TcpFeedbackItem createChannelDisconnect(int channelId) {
        return new TcpFeedbackItem(channelId, 0, TypeEnum.CHANNEL_DISCONNECT, null);
    }

    /**
     * 创建「通道无响应」反馈消息对象
     *
     * @param channelId 通道 ID
     * @return 已创建的反馈消息对象
     */
    public static TcpFeedbackItem createChannelNoResponse(int channelId) {
        return new TcpFeedbackItem(channelId, 0, TypeEnum.DEVICE_GROUP_NO_RESPONSE, null);
    }

    /**
     * 创建「消息重发次数超出限制」反馈消息对象
     *
     * @param baseMsg 出错的消息对象
     * @return 已创建的反馈消息对象
     */
    public static TcpFeedbackItem createResendOutBound(BaseMsg baseMsg) {
        return new TcpFeedbackItem(baseMsg.getGroupId(), baseMsg.getDeviceId(), TypeEnum.RESEND_OUT_BOUND, baseMsg);
    }

    /**
     * 创建「设备工作状态异常」反馈消息对象
     *
     * @param baseMsg 工作状态响应消息对象
     * @return 已创建的反馈消息对象
     */
    public static TcpFeedbackItem createDeviceWorkException(BaseMsg baseMsg) {
        return new TcpFeedbackItem(baseMsg.getGroupId(), baseMsg.getDeviceId(), TypeEnum.WORK_STATUS_EXCEPTION, baseMsg);
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public Instant getInstant() {
        return instant;
    }

    public TypeEnum getType() {
        return type;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public String getDescription() {
        return description;
    }

    public BaseMsg getBaseMsg() {
        return baseMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TcpFeedbackItem that = (TcpFeedbackItem) o;
        return groupId == that.groupId &&
                deviceId == that.deviceId &&
                type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, deviceId, type);
    }

    private void calculateTime() {
        instant = Instant.now();
        milliseconds = instant.toEpochMilli();
        LocalDateTime now = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate nowDate = now.toLocalDate();
        LocalTime nowTime = now.toLocalTime();
        currentDate = nowDate.format(DateTimeFormatter.ISO_DATE);
        currentTime = nowTime.format(DateTimeFormatter.ISO_TIME).split("\\.")[0];
    }
}
