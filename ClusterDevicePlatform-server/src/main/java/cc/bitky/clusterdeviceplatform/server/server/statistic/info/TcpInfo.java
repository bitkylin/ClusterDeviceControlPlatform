package cc.bitky.clusterdeviceplatform.server.server.statistic.info;

import cc.bitky.clusterdeviceplatform.server.config.CommSetting;

public class TcpInfo {
    /**
     * 待发送缓冲队列中，帧发送间隔「单位/ms」
     */
    private int frameSendInterval;
    /**
     * 已发送帧后，在该间隔时间后，检测接收回复帧的状态，用于检错重发功能「单位/s」
     */
    private int detectInterval;
    /**
     * 收到一个已激活的 Channel，直到校验结束所需要的时间「单位/s」
     */
    private int commDelay;
    /**
     * 检错重发最大次数，服务器向 TCP 通道发送 CAN 帧，最大重复发送次数
     */
    private int autoRepeatTimes;
    /**
     * 当设备中记录的剩余充电次数小于该值时，则向设备发送剩余充电次数
     */
    private int remainChargeTimes;

    public TcpInfo() {
        this.frameSendInterval = CommSetting.FRAME_SEND_INTERVAL;
        this.detectInterval = CommSetting.FRAME_SENT_TO_DETECT_INTERVAL;
        this.commDelay = CommSetting.ACCESSIBLE_CHANNEL_REPLY_INTERVAL;
        this.autoRepeatTimes = CommSetting.AUTO_REPEAT_REQUEST_TIMES;
        this.remainChargeTimes = CommSetting.DEPLOY_REMAIN_CHARGE_TIMES;
    }

    public int getFrameSendInterval() {
        return frameSendInterval;
    }

    public int getDetectInterval() {
        return detectInterval;
    }

    public int getCommDelay() {
        return commDelay;
    }

    public int getAutoRepeatTimes() {
        return autoRepeatTimes;
    }

    public int getRemainChargeTimes() {
        return remainChargeTimes;
    }
}
