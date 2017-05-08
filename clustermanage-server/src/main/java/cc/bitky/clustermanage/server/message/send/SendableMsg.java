package cc.bitky.clustermanage.server.message.send;

import cc.bitky.clustermanage.server.message.PriorityType;
import cc.bitky.clustermanage.server.message.base.ISendableMsg;
import cc.bitky.clustermanage.server.schedule.MsgKey;

public class SendableMsg implements ISendableMsg {
    private MsgKey msgKey;
    private byte[] bytes;
    private PriorityType priorityType = PriorityType.LOW;
    private byte sendTimes = 0;

    public SendableMsg(byte[] bytes) {
        msgKey = new MsgKey(bytes[4], bytes[3], bytes[2]);
        this.bytes = bytes;
    }

    @Override
    public MsgKey getMsgKey() {
        return msgKey;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    @Override
    public PriorityType getPriorityType() {
        return priorityType;
    }

    @Override
    public byte getSendTimes() {
        return sendTimes;
    }

    @Override
    public void increaseSendTimes() {
        sendTimes++;
    }
}
