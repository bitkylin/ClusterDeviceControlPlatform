package cc.bitky.clustermanage.utils;

public class ChargeStatusEnum {
    /**
     * 未初始化
     */
    public static final int UNINIT = 0;
    /**
     * 使用中
     */
    public static final int USING = 1;
    /**
     * 充电中
     */
    public static final int CHARGING = 2;
    /**
     * 已充满
     */
    public static final int FULL = 3;
    /**
     * 通信故障
     */
    public static final int TRAFFIC_ERROR = 4;

    /**
     * 充电故障
     */
    public static final int CHARGE_ERROR = 5;

    /**
     * 多种故障
     */
    public static final int CRASH = 6;
}
