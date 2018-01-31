package cc.bitky.clusterdeviceplatform.demo.tcp.statistic.except;

/**
 * 类型信息枚举
 */
public enum TypeEnum {

    RESEND_OUT_BOUND("消息重发次数超出限制"),

    CHANNEL_DISCONNECT("通道连接已断开"),

    WORK_STATUS_EXCEPTION("设备工作状态异常"),

    DEVICE_GROUP_NO_RESPONSE("设备组无响应");

    private String detail;

    TypeEnum(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
}
