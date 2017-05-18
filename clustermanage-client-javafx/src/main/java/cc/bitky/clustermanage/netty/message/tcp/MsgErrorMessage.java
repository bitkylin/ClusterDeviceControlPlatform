package cc.bitky.clustermanage.netty.message.tcp;


import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseMessage;

public class MsgErrorMessage extends BaseMessage {

    private String msg;

    public MsgErrorMessage(int groupId) {
        super(groupId);
        setMsgId(MsgType.ERROR);
    }

    public MsgErrorMessage() {
        this(-1);
    }

    private MsgErrorMessage(int groupId, String msg) {
        this(groupId);
        this.msg = msg;
    }

    public MsgErrorMessage(String msg) {
        this(-1, msg);
    }
}
