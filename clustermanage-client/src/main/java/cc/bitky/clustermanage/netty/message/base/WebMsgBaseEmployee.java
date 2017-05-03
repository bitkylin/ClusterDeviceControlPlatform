package cc.bitky.clustermanage.netty.message.base;

public class WebMsgBaseEmployee extends BaseMessage {

    String value;

    protected WebMsgBaseEmployee(int groupId, int boxId, String value) {
        super(groupId, boxId);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
