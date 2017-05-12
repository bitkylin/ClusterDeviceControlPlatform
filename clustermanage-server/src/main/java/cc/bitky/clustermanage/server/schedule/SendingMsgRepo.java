package cc.bitky.clustermanage.server.schedule;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingDeque;

import cc.bitky.clustermanage.server.message.send.SendableMsg;
import io.netty.util.HashedWheelTimer;

@Repository
public class SendingMsgRepo {

    private HashMap<MsgKey, byte[]> MsgHashMap = new HashMap<>(65536);
    private HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();
    private LinkedBlockingDeque<SendableMsg> linkedBlockingDeque = new LinkedBlockingDeque<>(655360);

    public HashMap<MsgKey, byte[]> getMsgHashMap() {
        return MsgHashMap;
    }

    public HashedWheelTimer getHashedWheelTimer() {
        return hashedWheelTimer;
    }

    public LinkedBlockingDeque<SendableMsg> getLinkedBlockingDeque() {
        return linkedBlockingDeque;
    }
}
