package cc.bitky.clusterdeviceplatform.demo.db.statistic.repo;

import java.util.concurrent.atomic.AtomicLong;

public class ProcessedMsgRepo {
    /**
     * 已收到的消息总数
     */
    public static final AtomicLong MSG_COUNT = new AtomicLong(0);
    /**
     * 已收到的充电状态帧总数
     */
    public static final AtomicLong MSG_CHARGE_COUNT = new AtomicLong(0);
    /**
     * 状态已改变的充电状态帧总数
     */
    public static final AtomicLong MSG_CHARGE_COUNT_FIXED = new AtomicLong(0);
    /**
     * 状态未改变的充电状态帧总数
     */
    public static final AtomicLong MSG_CHARGE_COUNT_VARIABLE = new AtomicLong(0);
    /**
     * 已收到的工作状态帧总数
     */
    public static final AtomicLong MSG_WORK_COUNT = new AtomicLong(0);
    /**
     * 状态已改变的工作状态帧总数
     */
    public static final AtomicLong MSG_WORK_COUNT_FIXED = new AtomicLong(0);
    /**
     * 状态未改变的工作状态帧总数
     */
    public static final AtomicLong MSG_WORK_COUNT_VARIABLE = new AtomicLong(0);
}
