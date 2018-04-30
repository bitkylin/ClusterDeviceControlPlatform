package cc.bitky.clusterdeviceplatform.server.web.spa.tcp.bean;

import java.util.Collections;
import java.util.List;

public class BaseMsgSending {

    private int msgCount;
    private int msgSendingCount;
    private List<MsgSending> msgSendings = Collections.emptyList();

    protected BaseMsgSending(int msgCount, int msgSendingCount) {
        this.msgCount = msgCount;
        this.msgSendingCount = msgSendingCount;
    }

    public int getMsgCount() {
        return msgCount;
    }

    protected void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public int getMsgSendingCount() {
        return msgSendingCount;
    }

    protected void setMsgSendingCount(int msgSendingCount) {
        this.msgSendingCount = msgSendingCount;
    }

    public List<MsgSending> getMsgSendings() {
        return msgSendings;
    }

    protected void setMsgSendings(List<MsgSending> msgSendings) {
        this.msgSendings = msgSendings;
    }
}
