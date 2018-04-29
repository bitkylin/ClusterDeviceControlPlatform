package cc.bitky.clusterdeviceplatform.server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.device.MsgCodecDeviceRemainChargeTimes;
import cc.bitky.clusterdeviceplatform.server.config.CommSetting;
import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.config.WebSetting;
import cc.bitky.clusterdeviceplatform.server.db.DbPresenter;
import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.db.statistic.repo.ProcessedMsgRepo;
import cc.bitky.clusterdeviceplatform.server.server.repo.DeviceStatusRepository;
import cc.bitky.clusterdeviceplatform.server.server.repo.MsgProcessingRepository;
import cc.bitky.clusterdeviceplatform.server.server.repo.TcpFeedBackRepository;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.except.TcpFeedbackItem;

@Service
public class ServerCenterProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ServerCenterProcessor.class);
    private final MsgProcessingRepository msgProcessingRepository;
    private final TcpFeedBackRepository tcpFeedBackRepository;
    private final DeviceStatusRepository deviceStatusRepository;
    private final DbPresenter dbPresenter;
    private ServerTcpProcessor tcpProcessor;

    @Autowired
    public ServerCenterProcessor(MsgProcessingRepository msgProcessingRepository, DbPresenter dbPresenter, TcpFeedBackRepository tcpFeedBackRepository, DeviceStatusRepository deviceStatusRepository) {
        this.msgProcessingRepository = msgProcessingRepository;
        this.tcpFeedBackRepository = tcpFeedBackRepository;
        this.deviceStatusRepository = deviceStatusRepository;
        this.dbPresenter = dbPresenter;
        msgProcessingRepository.setDbPresenter(dbPresenter);
        msgProcessingRepository.setServer(this);
        addJvmShutDownHook();
    }


    public DbPresenter getDbPresenter() {
        return dbPresenter;
    }

    public DeviceStatusRepository getDeviceStatusRepository() {
        return deviceStatusRepository;
    }

    public TcpFeedBackRepository getTcpFeedBackRepository() {
        return tcpFeedBackRepository;
    }

    public MsgProcessingRepository getMsgProcessingRepository() {
        return msgProcessingRepository;
    }

    ServerTcpProcessor getTcpProcessor() {
        return tcpProcessor;
    }

    void setTcpProcessor(ServerTcpProcessor tcpProcessor) {
        this.tcpProcessor = tcpProcessor;
    }

    private boolean sendMessage(BaseMsg message) {
        if (message == null) {
            return false;
        }
        deviceStatusRepository.saveMsg(message);
        return tcpProcessor.sendMessage(message);
    }

    /**
     * 将「消息对象」下发至 TCP，若为成组消息可自动解包
     *
     * @param message 欲下发的消息
     * @return 是否下发成功
     */
    boolean sendMessageGrouped(BaseMsg message) {
        int groupId = message.getGroupId();
        int deviceId = message.getDeviceId();

        if (groupId == WebSetting.BROADCAST_GROUP_ID && deviceId == WebSetting.BROADCAST_DEVICE_ID) {
            boolean success = false;
            for (int tempGroupId = 1; tempGroupId <= DeviceSetting.MAX_GROUP_ID; tempGroupId++) {
                if (tcpProcessor.tcpIsAvailable(tempGroupId)) {
                    for (int tempDeviceId = 1; tempDeviceId <= DeviceSetting.MAX_DEVICE_ID; tempDeviceId++) {
                        if (sendMessage(message.clone(tempGroupId, tempDeviceId))) {
                            success = true;
                        }
                    }
                }
            }
            return success;
        }

        if (groupId == WebSetting.BROADCAST_GROUP_ID) {
            boolean success = false;
            for (int tempGroupId = 1; tempGroupId <= DeviceSetting.MAX_GROUP_ID; tempGroupId++) {
                if (tcpProcessor.tcpIsAvailable(tempGroupId) && sendMessage(message.clone(tempGroupId, deviceId))) {
                    success = true;
                }
            }
            return success;
        }

        if (!tcpProcessor.tcpIsAvailable(groupId)) {
            return false;
        }

        if (deviceId == WebSetting.BROADCAST_DEVICE_ID) {
            boolean success = true;
            for (int tempDeviceId = 1; tempDeviceId <= DeviceSetting.MAX_DEVICE_ID; tempDeviceId++) {
                if (!sendMessage(message.clone(groupId, tempDeviceId))) {
                    success = false;
                }
            }
            return success;
        }

        return sendMessage(message);
    }

    /**
     * 添加关闭系统的钩子
     */
    private void addJvmShutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.warn("开始关闭服务器");
            tcpProcessor.shutDown();
            try {
                Thread.sleep(CommSetting.ACCESSIBLE_CHANNEL_REPLY_INTERVAL);
            } catch (InterruptedException ignored) {
            }
            logger.warn("服务器优雅关闭成功");
        }));
    }

    /**
     * 服务器从TCP捕获到设备状态消息对象，并送入缓冲区等待处理
     *
     * @param message 设备状态消息对象
     */
    public void huntDeviceStatusMsg(MsgReplyDeviceStatus message) {
        ProcessedMsgRepo.MSG_COUNT.incrementAndGet();
        LinkedBlockingDeque<MsgReplyDeviceStatus> deque = msgProcessingRepository.touchMessageQueue(message.getGroupId());
        if (deque == null) {
            logger.warn("待处理的「状态消息」执行队列不存在，id:" + message.getGroupId());
            return;
        }
        deque.offer(message);
    }

    /**
     * 部署剩余充电次数
     *
     * @param device 处理后的 Device
     */
    public void deployRemainChargeTimes(Device device) {
        //当前充电状态为「充满」，并且剩余充电次数小于或等于阈值时，部署剩余充电次数
        if (device.getChargeStatus() == ChargeStatus.FULL && device.getRemainChargeTime() <= CommSetting.DEPLOY_REMAIN_CHARGE_TIMES) {
            int remainTimes = device.getRemainChargeTime();
            remainTimes = remainTimes > 0 ? remainTimes : 0;
            sendMessage(MsgCodecDeviceRemainChargeTimes.create(device.getGroupId(), device.getDeviceId(), remainTimes));
        }
    }

    /**
     * 根据条件对List中的Item进行过滤，返回状态异常消息或超时消息
     *
     * @param type 请求类型
     * @return 指定的返回的消息集合
     */
    public List<TcpFeedbackItem> getTcpFeedBackItems(TcpFeedBackRepository.ItemType type) {
        return getTcpFeedBackRepository().getItems(type);
    }

    /**
     * 根据条件对List中的Item进行过滤，返回状态异常消息或超时消息的数量
     *
     * @param type 请求类型
     * @return 指定的返回的消息的数量
     */
    public long getTcpFeedBackItemsCount(TcpFeedBackRepository.ItemType type) {
        return getTcpFeedBackRepository().getItemsCount(type);
    }
}
