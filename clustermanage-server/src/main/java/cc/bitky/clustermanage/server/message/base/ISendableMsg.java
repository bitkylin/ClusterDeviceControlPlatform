package cc.bitky.clustermanage.server.message.base;


import cc.bitky.clustermanage.server.schedule.MsgKey;

public interface ISendableMsg {
    byte getSendTimes();

    void increaseSendTimes();

    public MsgKey getMsgKey();

    public byte[] getBytes();
}
