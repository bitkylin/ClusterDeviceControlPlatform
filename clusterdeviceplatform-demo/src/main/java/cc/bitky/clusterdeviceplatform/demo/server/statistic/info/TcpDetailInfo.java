package cc.bitky.clusterdeviceplatform.demo.server.statistic.info;

import cc.bitky.clusterdeviceplatform.demo.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.messageutils.config.FrameSetting;

public class TcpDetailInfo {
    /**
     * 最大设备组号
     */
    private int maxGroupId;
    /**
     * 设备组内的最大设备号
     */
    private int maxDeviceId;
    /**
     * 主帧帧头长度
     */
    private int frameHeadLength;
    /**
     * 主帧数据体的最大长度
     */
    private int maxDataLength;
    /**
     * 子帧帧头长度
     */
    private int subFrameHeadLength;
    /**
     * 从 Channel 处理线程被激活，直到开始处理，所经过的用于容纳所有子帧的时间「单位:ms」
     */
    private int awakeToProcessInterval;

    public TcpDetailInfo() {
        this.maxGroupId = DeviceSetting.MAX_GROUP_ID;
        this.maxDeviceId = DeviceSetting.MAX_DEVICE_ID;
        this.frameHeadLength = FrameSetting.FRAME_HEAD_LENGTH;
        this.subFrameHeadLength = FrameSetting.SUB_FRAME_HEAD_LENGTH;
        this.maxDataLength = FrameSetting.MAX_DATA_LENGTH;
        this.awakeToProcessInterval = FrameSetting.AWAKE_TO_PROCESS_INTERVAL;
    }

    public int getMaxGroupId() {
        return maxGroupId;
    }

    public int getMaxDeviceId() {
        return maxDeviceId;
    }

    public int getFrameHeadLength() {
        return frameHeadLength;
    }

    public int getSubFrameHeadLength() {
        return subFrameHeadLength;
    }

    public int getMaxDataLength() {
        return maxDataLength;
    }

    public int getAwakeToProcessInterval() {
        return awakeToProcessInterval;
    }
}
