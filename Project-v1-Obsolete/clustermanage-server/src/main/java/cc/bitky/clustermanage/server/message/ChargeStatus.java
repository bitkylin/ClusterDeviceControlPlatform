package cc.bitky.clustermanage.server.message;

public interface ChargeStatus {

    /**
     * 未初始化
     */
    int UNINIT = 0;

    /**
     * 使用中
     */
    int USING = 1;

    /**
     * 充电中
     */
    int CHARGING = 2;

    /**
     * 已充满
     */
    int FULL = 3;

    /**
     * 通信故障
     */
    int TRAFFIC_ERROR = 4;

    /**
     * 充电故障
     */
    int CHARGE_ERROR = 5;

    /**
     * 矿灯未挂好
     */
    int HUNG_ERROR = 6;

    /**
     * 多种故障
     */
    int CRASH = 50;
}
