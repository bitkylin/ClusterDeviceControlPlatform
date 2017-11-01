package cc.bitky.clustermanage.server.message.tcp;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;

public class MsgErrorMessage extends BaseMessage {

    private String msg;

    private MsgErrorMessage(int groupId) {
        super(groupId);
        setMsgId(MsgType.ERROR);
    }

    private MsgErrorMessage(int groupId, String msg) {
        this(groupId);
        this.msg = msg;
    }

    public MsgErrorMessage(String msg) {
        this(-1, msg);
    }
}
