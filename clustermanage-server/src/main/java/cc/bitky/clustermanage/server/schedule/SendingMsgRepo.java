package cc.bitky.clustermanage.server.schedule;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

import io.netty.util.HashedWheelTimer;

@Repository
public class SendingMsgRepo {

    HashMap<MsgKey, byte[]> MsgHashMap = new HashMap<>(65536);
    HashedWheelTimer hashedWheelTimer = new HashedWheelTimer();

    public HashMap<MsgKey, byte[]> getMsgHashMap() {
        return MsgHashMap;
    }

    public HashedWheelTimer getHashedWheelTimer() {
        return hashedWheelTimer;
    }
}
