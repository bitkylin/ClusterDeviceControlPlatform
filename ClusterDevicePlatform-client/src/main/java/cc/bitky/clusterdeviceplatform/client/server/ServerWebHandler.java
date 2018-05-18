package cc.bitky.clusterdeviceplatform.client.server;

import org.springframework.stereotype.Service;

import java.util.Random;

import cc.bitky.clusterdeviceplatform.client.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.client.server.statistic.ProcessedMsgRepo;
import cc.bitky.clusterdeviceplatform.client.ui.bean.Device;
import cc.bitky.clusterdeviceplatform.client.ui.bean.DeviceCellRepo;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.statusreply.MsgCodecReplyStatusCharge;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.statusreply.MsgCodecReplyStatusWork;

@Service
public class ServerWebHandler {
    private Random random = new Random();
    private ServerTcpHandler tcp;

    public ServerWebHandler(ServerTcpHandler tcp) {
        this.tcp = tcp;
    }


    public ServerTcpHandler getTcp() {
        return tcp;
    }

    public void setTcp(ServerTcpHandler tcp) {
        this.tcp = tcp;
    }

    private void createNeedMsg(int probability, int errorProbability, int count) {
        while (count > 0) {
            count--;
            for (int groupId = 1; groupId <= DeviceSetting.MAX_GROUP_ID; groupId++) {
                for (int deviceId = 1; deviceId <= DeviceSetting.MAX_DEVICE_ID; deviceId++) {
                    Device device = DeviceCellRepo.getDevice(groupId, deviceId);
                    int chargeFactor = random.nextInt(100);
                    if (chargeFactor < probability) {
                        int status = device.getChargeStatus();
                        int newStatus = status;
                        while (newStatus == status || newStatus < 1) {
                            newStatus = random.nextInt(4);
                        }
                        device.setChargeStatus(newStatus);
                        ProcessedMsgRepo.MSG_CHARGE_COUNT_VARIABLE.incrementAndGet();
                    } else {
                        ProcessedMsgRepo.MSG_CHARGE_COUNT_FIXED.incrementAndGet();
                    }

                    int workFactor = random.nextInt(100);
                    if (workFactor < errorProbability) {
                        int newStatus = random.nextInt(11);
                        if (newStatus < 4) {
                            newStatus = 50;
                        }
                        device.setWorkStatus(newStatus);
                        ProcessedMsgRepo.MSG_WORK_COUNT_VARIABLE.incrementAndGet();
                    } else {
                        ProcessedMsgRepo.MSG_WORK_COUNT_FIXED.incrementAndGet();
                        device.setWorkStatus(1);
                    }

                    tcp.sendMessage(MsgCodecReplyStatusCharge.create(groupId, deviceId, device.getChargeStatus(), System.currentTimeMillis()));
                    ProcessedMsgRepo.MSG_CHARGE_COUNT.incrementAndGet();
                    ProcessedMsgRepo.MSG_COUNT.incrementAndGet();
                    tcp.sendMessage(MsgCodecReplyStatusWork.create(groupId, deviceId, device.getWorkStatus(), System.currentTimeMillis()));
                    ProcessedMsgRepo.MSG_WORK_COUNT.incrementAndGet();
                    ProcessedMsgRepo.MSG_COUNT.incrementAndGet();
                }
            }
        }
    }

    public void runTest1() {
        createNeedMsg(100, 100, 100);
    }
}
