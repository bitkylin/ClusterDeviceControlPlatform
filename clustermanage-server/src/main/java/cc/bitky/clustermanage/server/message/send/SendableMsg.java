package cc.bitky.clustermanage.server.message.send;

import cc.bitky.clustermanage.server.message.base.ISendableMsg;
import cc.bitky.clustermanage.server.schedule.MsgKey;
import io.netty.channel.ChannelHandlerContext;

//保持发送状态、发送规则、和消息的 bean
public class SendableMsg implements ISendableMsg {
    private final MsgKey msgKey;
    private final byte[] bytes;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    ChannelHandlerContext ctx;


    private boolean responsive = true;
    private boolean urgent = false;
    private byte sendTimes = 0;

    public SendableMsg(byte[] bytes) {
        msgKey = new MsgKey(bytes[4], bytes[3], bytes[2]);
        this.bytes = bytes;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public boolean isResponsive() {
        return responsive;
    }

    public void setResponsive(boolean responsive) {
        this.responsive = responsive;
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
    public byte getSendTimes() {
        return sendTimes;
    }

    @Override
    public void increaseSendTimes() {
        sendTimes++;
    }
}
