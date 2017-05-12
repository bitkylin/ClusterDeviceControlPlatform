package cc.bitky.clustermanage.server.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import cc.bitky.clustermanage.ServerSetting;
import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.db.presenter.KyDbPresenter;
import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseTcpResponseMsg;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.send.WebMsgSpecial;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgInitResponseCardNumber;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseStatus;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployRemainChargeTimes;
import cc.bitky.clustermanage.server.message.web.WebMsgInitMarchConfirmCardResponse;
import cc.bitky.clustermanage.server.schedule.MsgKey;
import cc.bitky.clustermanage.server.schedule.SendingMsgRepo;
import cc.bitky.clustermanage.tcp.server.netty.SendWebMessagesListener;

@Service
public class ServerTcpMessageHandler {
    private final KyDbPresenter kyDbPresenter;
    private final SendingMsgRepo sendingMsgRepo;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private SendWebMessagesListener sendWebMessagesListener;
    private KyServerCenterHandler kyServerCenterHandler;

    @Autowired
    public ServerTcpMessageHandler(KyDbPresenter kyDbPresenter, SendingMsgRepo sendingMsgRepo) {
        this.kyDbPresenter = kyDbPresenter;
        this.sendingMsgRepo = sendingMsgRepo;
    }

    public SendingMsgRepo getSendingMsgRepo() {
        return sendingMsgRepo;
    }

    /**
     * 设备状态回复处理方法
     *
     * @param message 设备状态回复 bean
     */
    public void handleResDeviceStatus(TcpMsgResponseStatus message) {
        logger.info("");
        logger.info("");
        logger.info("***********进入功能消息处理方法「" + message.getGroupId() + ", " + message.getBoxId() + "」***********");
        if (message.getResSource() == TcpMsgResponseStatus.ResSource.SERVER)
            logger.info("收到：服务器监测到的设备状态");
        else
            logger.info("收到：设备状态请求的回复");
        long l1 = System.currentTimeMillis();
        Device device = kyDbPresenter.handleMsgDeviceStatus(message, ServerSetting.AUTO_CREATE_DEVICE_EMPLOYEE);
        if (device != null) {
            deployRemainChargeTimes(device);
        }
        long l2 = System.currentTimeMillis();
        logger.info("处理设备状态包花费的总时间：" + (l2 - l1) + "ms");
    }

    /**
     * 其他功能消息处理方法
     *
     * @param message 功能消息 bean
     */
    public void handleTcpMsg(IMessage message) {

    }

    /**
     * 返回剩余充电次数
     *
     * @param device 处理后的 Device
     */

    private void deployRemainChargeTimes(Device device) {
        if (device.getRemainChargeTime() <= ServerSetting.DEPLOY_REMAIN_CHARGE_TIMES) {
            int remainTimes = device.getRemainChargeTime();
            remainTimes = remainTimes > 0 ? remainTimes : 0;
            sendMsgToTcpSpecial(new WebMsgDeployRemainChargeTimes(device.getGroupId(), device.getBoxId(), remainTimes), true, true);
        }
    }

    /**
     * 常规回复消息处理方法
     *
     * @param message 常规回复消息 bean
     */
    public void handleTcpResponseMsg(BaseTcpResponseMsg message) {
        logger.info("");
        logger.info("");
        logger.info("***********进入常规回复消息处理方法「" + message.getGroupId() + ", " + message.getBoxId()
                + ", " + message.getMsgId() + "『" + message.getStatus() + "』」***********");
        switch (message.getMsgId()) {
            case MsgType.DEVICE_RESPONSE_REMAIN_CHARGE_TIMES:
                logger.info("收到：充电次数回复");
                break;
            case MsgType.DEVICE_RESPONSE_DEVICE_ID:
                logger.info("收到：柜号更新回复");
                break;
            case MsgType.DEVICE_RESPONSE_EMPLOYEE_NAME:
                logger.info("收到：姓名更新回复");
                break;
            case MsgType.DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_1:
                logger.info("收到：单位「1」更新回复");
                break;
            case MsgType.DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_2:
                logger.info("收到：单位「2」更新回复");
                break;
            case MsgType.DEVICE_RESPONSE_EMPLOYEE_CARD_NUMBER:
                logger.info("收到：卡号更新回复");
                break;
            case MsgType.DEVICE_RESPONSE_REMOTE_UNLOCK:
                logger.info("收到：远程开锁回复");
                break;
        }
        if (message.getMsgId() >= ((byte) 0x80) && message.getMsgId() <= ((byte) 0x8F)) {
            logger.info("收到：万能卡号更新回复");
        }


        if (message.getMsgId() > 0x40 && message.getMsgId() <= 0x4F) {
            if (message.getStatus() == 1) {
                byte groupId = (byte) message.getGroupId();
                byte boxId = (byte) message.getBoxId();
                byte msgId = (byte) (message.getMsgId() - 48);

                MsgKey msgKey = new MsgKey(groupId, boxId, msgId);
                if (sendingMsgRepo.getMsgHashMap().remove(msgKey) != null) {
                    logger.info("已从哈希表移除");
                } else {
                    logger.info("哈希表无该 Key");
                }
            } else logger.info("回复设备接收");
            return;
        }

        if (message.getMsgId() >= ((byte) 0x80) && message.getMsgId() <= ((byte) 0x8F)) {
            if (message.getStatus() == 1) {
                byte groupId = (byte) message.getGroupId();
                byte boxId = (byte) message.getBoxId();
                byte msgId = (byte) ((message.getMsgId() & 0x0F) + 0x70);

                MsgKey msgKey = new MsgKey(groupId, boxId, msgId);
                if (sendingMsgRepo.getMsgHashMap().remove(msgKey) != null) {
                    logger.info("已从哈希表移除");
                } else {
                    logger.info("哈希表无该 Key");
                }
            } else logger.info("回复设备接收");
        }

    }

    /**
     * 初始化消息处理方法
     *
     * @param message 初始化消息 bean
     */
    public void handleTcpInitMsg(IMessage message) {
        logger.info("");
        logger.info("");
        logger.info("***********进入初始化消息处理方法「" + message.getGroupId() + ", " + message.getBoxId()
                + ", " + message.getMsgId() + "」***********");

        //从数据库中匹配员工卡号或确认卡号，获取相应信息并发送至 Netty 的 Handler
        if (message.getMsgId() == MsgType.INITIALIZE_DEVICE_RESPONSE_CARD) {
            long l1 = System.currentTimeMillis();
            handleReceivedCard(message);
            long l2 = System.currentTimeMillis();
            logger.info("处理设备初始化包花费的总时间：" + (l2 - l1) + "ms" + "\n\n");
        }
    }

    /**
     * 设备主动发送卡号的处理方法, 返回匹配确认卡号的结果
     *
     * @param message 「初始化」设备主动发送卡号 Message
     */
    private void handleReceivedCard(IMessage message) {
        TcpMsgInitResponseCardNumber msgInitCard = (TcpMsgInitResponseCardNumber) message;
        logger.info("收到：设备发送的卡号: " + msgInitCard.getCardNumber());


        //卡号初始化为 0，故排除掉 0 以避免错误
        if (msgInitCard.getCardNumber() == 0) {
            sendMsgToTcpSpecial(new WebMsgInitMarchConfirmCardResponse(msgInitCard.getGroupId(), msgInitCard.getBoxId(), false), true, false);
            return;
        }


        if (kyServerCenterHandler.marchConfirmCard(msgInitCard.getCardNumber())) {
            sendMsgToTcpSpecial(new WebMsgInitMarchConfirmCardResponse(msgInitCard.getGroupId(), msgInitCard.getBoxId(), true), true, false);
        } else {
            sendMsgToTcpSpecial(new WebMsgInitMarchConfirmCardResponse(msgInitCard.getGroupId(), msgInitCard.getBoxId(), false), true, false);
        }
    }

    public void setSendWebMessagesListener(SendWebMessagesListener sendWebMessagesListener) {
        this.sendWebMessagesListener = sendWebMessagesListener;
    }

    /**
     * 「特殊的」将特殊的 Message 发送至 Netty 的处理通道
     *
     * @param message    普通消息 Message
     * @param urgent     紧急的
     * @param responsive 需要检错重发回复的
     * @return 是否发送成功
     */
    private boolean sendMsgToTcpSpecial(IMessage message, boolean urgent, boolean responsive) {
        WebMsgSpecial msgSpecial = new WebMsgSpecial(message);
        msgSpecial.setUrgent(urgent);
        msgSpecial.setResponsive(responsive);
        return sendWebMessagesListener.sendMessagesToTcp(msgSpecial);
    }

    /**
     * 直接将 Message 发送至 Netty 的处理通道
     *
     * @param message 普通消息 Message
     * @return 是否发送成功
     */
    private boolean sendMsgToTcp(IMessage message) {
        if (sendWebMessagesListener == null) {
            logger.warn("Server 模块未能与 Netty 模块建立连接，故不能发送消息集合");
            return false;
        }
        return sendWebMessagesListener.sendMessagesToTcp(message);
    }

    /**
     * 拥塞控制，若队列中等待发送的帧过多，则将这些 Message 添加至时间轮中
     *
     * @return 是否发送成功或添加时间轮成功
     */
    boolean sendMsgTrafficControl(IMessage message) {
        if (sendingMsgRepo.getLinkedBlockingDeque().size() > ServerSetting.LINKED_DEQUE_LIMIT_CAPACITY) {
            sendingMsgRepo.getHashedWheelTimer()
                    .newTimeout(timeout -> sendMsgTrafficControl(message), ServerSetting.COMMAND_DELAY_WAITING_TIME, TimeUnit.SECONDS);
            return true;
        }
        return sendMsgToTcp(message);
    }

    void setKyServerCenterHandler(KyServerCenterHandler kyServerCenterHandler) {
        this.kyServerCenterHandler = kyServerCenterHandler;
    }
}
