package cc.bitky.clusterdeviceplatform.server.db.statistic.pressure;

import cc.bitky.clusterdeviceplatform.server.db.statistic.repo.ProcessedMsgRepo;
import lombok.Data;

/**
 * 待处理及已处理消息统计计数
 */
@Data
public class MsgCount {
    /**
     * 已收到的消息总数
     */
    private long msgCount;
    /**
     * 已收到的充电状态帧总数
     */
    private long msgChargeCount;
    /**
     * 状态已改变的充电状态帧总数
     */
    private long msgChargeCountFixed;
    /**
     * 状态未改变的充电状态帧总数
     */
    private long msgChargeCountVariable;
    /**
     * 已收到的工作状态帧总数
     */
    private long msgWorkCount;
    /**
     * 状态已改变的工作状态帧总数
     */
    private long msgWorkCountFixed;
    /**
     * 状态未改变的工作状态帧总数
     */
    private long msgWorkCountVariable;

    public MsgCount() {
        this.msgCount = ProcessedMsgRepo.MSG_COUNT.get();
        this.msgChargeCount = ProcessedMsgRepo.MSG_CHARGE_COUNT.get();
        this.msgChargeCountFixed = ProcessedMsgRepo.MSG_CHARGE_COUNT_FIXED.get();
        this.msgChargeCountVariable = ProcessedMsgRepo.MSG_CHARGE_COUNT_VARIABLE.get();
        this.msgWorkCount = ProcessedMsgRepo.MSG_WORK_COUNT.get();
        this.msgWorkCountFixed = ProcessedMsgRepo.MSG_WORK_COUNT_FIXED.get();
        this.msgWorkCountVariable = ProcessedMsgRepo.MSG_WORK_COUNT_VARIABLE.get();
    }
}
