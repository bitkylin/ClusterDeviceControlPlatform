package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;

/**
 * 设备回复: 充电状态
 */
public class TcpMsgResponseStatus extends BaseMessage {
    private ResSource resSource = ResSource.DEVICE;
    private int status;
    private long time;

    public TcpMsgResponseStatus(int groupId, int deviceId, int status) {
        super(groupId, deviceId);
        this.status = status;
        this.time = System.currentTimeMillis();
        setMsgId(MsgType.DEVICE_RESPONSE_STATUS);
    }

    public TcpMsgResponseStatus(int groupId, int boxId, int status, ResSource resSource) {
        this(groupId, boxId, status);
        this.resSource = resSource;
    }

    public ResSource getResSource() {
        return resSource;
    }

    public int getStatus() {
        return status;
    }

    public long getTime() {
        return time;
    }

    /**
     * 该 bean 的来源
     */
    public enum ResSource {
        /**
         * 设备
         */
        DEVICE,
        /**
         * 服务器
         */
        SERVER
    }

}
