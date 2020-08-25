package cc.bitky.clusterdeviceplatform.server.server.demonstrate.msgprocess;

import java.util.Random;

import cc.bitky.clusterdeviceplatform.server.db.statistic.pressure.MsgCount;

/**
 * 待处理及已处理消息统计计数
 */
public class MsgCountRandom extends MsgCount {

    public MsgCountRandom(MsgCountRandom random) {
        if (random == null) {
            return;
        }
        Random r = new Random();
        setMsgChargeCountFixed(random.getMsgChargeCountFixed() + r.nextInt(100));
        setMsgChargeCountVariable(random.getMsgChargeCountVariable() + r.nextInt(100));
        setMsgWorkCountFixed(random.getMsgWorkCountFixed() + r.nextInt(100));
        setMsgWorkCountVariable(random.getMsgWorkCountVariable() + r.nextInt(100));
        setMsgChargeCount(getMsgChargeCountVariable() + getMsgChargeCountFixed());
        setMsgWorkCount(getMsgWorkCountVariable() + getMsgWorkCountFixed());
        setMsgCount(getMsgWorkCount() + getMsgChargeCount());
    }
}
