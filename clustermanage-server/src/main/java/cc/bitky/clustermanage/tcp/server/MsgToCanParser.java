package cc.bitky.clustermanage.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.send.SendableMsg;
import cc.bitky.clustermanage.server.message.send.WebMsgSpecial;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployFreeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployLedSetting;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployLedStop;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployRemainChargeTimes;
import cc.bitky.clustermanage.server.message.web.WebMsgInitClearDeviceStatus;
import cc.bitky.clustermanage.server.message.web.WebMsgInitMarchConfirmCardResponse;
import cc.bitky.clustermanage.server.message.web.WebMsgObtainDeviceStatus;
import cc.bitky.clustermanage.server.message.web.WebMsgOperateBoxUnlock;
import cc.bitky.clustermanage.tcp.TcpMediator;
import cc.bitky.clustermanage.tcp.util.TcpMsgBuilder;

/**
 * 服务器 Message 消息解析为 TCP 通道的 CAN 帧
 */
@Service
public class MsgToCanParser {

    private final TcpMsgBuilder tcpMsgBuilder;
    private final PolicyCanTransmitter policyCanTransmitter;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public MsgToCanParser(TcpMsgBuilder tcpMsgBuilder, PolicyCanTransmitter policyCanTransmitter, TcpMediator tcpMediator) {
        this.tcpMsgBuilder = tcpMsgBuilder;
        this.policyCanTransmitter = policyCanTransmitter;
        tcpMediator.setMsgToCanParser(this);
    }

    public void write(IMessage message) {
        logger.info("Netty 通道获取 Message「" + message.getGroupId() + ", " + message.getDeviceId() + ", " + message.getMsgId() + "」");

        if (message.getMsgId() == MsgType.SERVER_SEND_SPECIAL) {
            WebMsgSpecial msgSpecial = (WebMsgSpecial) message;
            logger.info("$$$$$$$$$$$$发送特殊帧「urgent:" + msgSpecial.isUrgent() + ", responsive:" + msgSpecial.isResponsive() + ", type:" + msgSpecial.getTpye().toString() + "」$$$$$$$$$$$$$$$");
            IMessage baseMsgSpecial = msgSpecial.getMessage();
            switch (msgSpecial.getTpye()) {
                case GROUP:
                    for (int k = 1; k <= msgSpecial.getMaxGroupId(); k++) {
                        baseMsgSpecial.setGroupId(k);
                        unpackComplexMsgToTcp(baseMsgSpecial, msgSpecial.isUrgent(), msgSpecial.isResponsive());
                    }
                    break;
                case BOX:
                    for (int j = 1; j <= msgSpecial.getMaxDeviceId(); j++) {
                        baseMsgSpecial.setDeviceId(j);
                        unpackComplexMsgToTcp(baseMsgSpecial, msgSpecial.isUrgent(), msgSpecial.isResponsive());
                    }
                    break;
                case ALL:
                    for (int j = 1; j <= msgSpecial.getMaxDeviceId(); j++) {
                        for (int k = 1; k <= msgSpecial.getMaxGroupId(); k++) {
                            baseMsgSpecial.setDeviceId(j);
                            baseMsgSpecial.setGroupId(k);
                            unpackComplexMsgToTcp(baseMsgSpecial, msgSpecial.isUrgent(), msgSpecial.isResponsive());
                        }
                    }
                    break;
                case NONE:
                    unpackComplexMsgToTcp(baseMsgSpecial, msgSpecial.isUrgent(), msgSpecial.isResponsive());
                    break;
            }

        } else {
            unpackComplexMsgToTcp(message, false, true);
        }
    }

    /**
     * 将非成组的特殊 Message 转化为 byte[] -> SendableMsg 并传入下一级通道
     *
     * @param message 信息 bean
     */
    private void unpackComplexMsgToTcp(IMessage message, boolean urgent, boolean responsive) {
        switch (message.getMsgId()) {
            case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_1:
                byte[] bytesDepartment = buildByMessage(message);
                byte[] bytesD1 = new byte[13];
                byte[] bytesD2 = new byte[13];
                System.arraycopy(bytesDepartment, 0, bytesD1, 0, 13);
                System.arraycopy(bytesDepartment, 13, bytesD2, 0, 13);
                deployWriteTcpSpecial(bytesD1, urgent, responsive);
                deployWriteTcpSpecial(bytesD2, urgent, responsive);
                return;

            case MsgType.SERVER_SET_FREE_CARD_NUMBER:
                byte[] bytesFree = buildByMessage(message);
                int count = bytesFree.length / 13;
                for (int i = 0; i < count; i++) {
                    byte[] bytesF = new byte[13];
                    System.arraycopy(bytesFree, 13 * i, bytesF, 0, 13);
                    bytesF[0] += 8;
                    deployWriteTcpSpecial(bytesF, urgent, responsive);
                }
                return;
            case MsgType.SERVER_LED_SETTING:
                WebMsgDeployLedSetting led = (WebMsgDeployLedSetting) message;
                logger.info("生成帧：LED参数设置");
                List<byte[]> list = tcpMsgBuilder.buildLedSetting(led);
                list.forEach(bytes -> deployWriteTcpSpecial(bytes, urgent, responsive));
                return;
        }
        deployWriteTcpSpecial(buildByMessage(message), urgent, responsive);
    }

    /**
     * 「特殊的」将 byte[] 转化为 SendableMsg 并传入下一级写出通道
     */
    private void deployWriteTcpSpecial(byte[] bytes, boolean urgent, boolean responsive) {
        if (bytes == null || bytes.length == 0) {
            logger.info("未生成 CAN 帧，该 Message 被丢弃");
            return;
        }
        SendableMsg sendableMsg = new SendableMsg(bytes);
        sendableMsg.setUrgent(urgent);
        sendableMsg.setResponsive(responsive);
        policyCanTransmitter.write(sendableMsg);
    }

    private byte[] buildByMessage(IMessage message) {
        switch (message.getMsgId()) {
            case MsgType.SERVER_REQUSET_STATUS:
                WebMsgObtainDeviceStatus obtainDeviceStatus = (WebMsgObtainDeviceStatus) message;
                logger.info("生成帧：请求设备当前状态");
                return tcpMsgBuilder.buildRequestDeviceStatus(obtainDeviceStatus);

            case MsgType.SERVER_SET_REMAIN_CHARGE_TIMES:
                WebMsgDeployRemainChargeTimes remainChargeTimes = (WebMsgDeployRemainChargeTimes) message;
                logger.info("生成帧：设置设备剩余充电次数: " + remainChargeTimes.getTimes());
                return tcpMsgBuilder.buildRemainChargeTimes(remainChargeTimes);

            case MsgType.SERVER_SET_DEVICE_ID:
                WebMsgDeployEmployeeDeviceId deployEmployeeDeviceId = (WebMsgDeployEmployeeDeviceId) message;
                logger.info("生成帧：设置设备 ID: " + deployEmployeeDeviceId.getUpdatedDeviceId());
                return tcpMsgBuilder.buildDeviceId(deployEmployeeDeviceId);

            case MsgType.SERVER_SET_EMPLOYEE_NAME:
                WebMsgDeployEmployeeName deployEmployeeName = (WebMsgDeployEmployeeName) message;
                logger.info("生成帧：设置员工姓名: " + deployEmployeeName.getValue());
                return tcpMsgBuilder.buildEmployeeName(deployEmployeeName);

            case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_1:
                WebMsgDeployEmployeeDepartment deployEmployeeDepartment = (WebMsgDeployEmployeeDepartment) message;
                logger.info("生成帧：设置员工单位: " + deployEmployeeDepartment.getValue());
                return tcpMsgBuilder.buildEmployeeDepartment(deployEmployeeDepartment);

            case MsgType.SERVER_SET_EMPLOYEE_CARD_NUMBER:
                WebMsgDeployEmployeeCardNumber deployEmployeeCardNumber = (WebMsgDeployEmployeeCardNumber) message;
                logger.info("生成帧：设置员工卡号: " + deployEmployeeCardNumber.getCardNumber());
                return tcpMsgBuilder.buildEmployeeCardNumber(deployEmployeeCardNumber);

            case MsgType.SERVER_REMOTE_UNLOCK:
                logger.info("生成帧：解锁单个设备");
                return tcpMsgBuilder.buildWebUnlock((WebMsgOperateBoxUnlock) message);

            case MsgType.SERVER_SET_FREE_CARD_NUMBER:
                logger.info("生成帧：设置万能卡号");
                return tcpMsgBuilder.buildFreeCardNumber((WebMsgDeployFreeCardNumber) message);

            case MsgType.SERVER_LED_STOP:
                logger.info("生成帧：LED停止");
                return tcpMsgBuilder.buildMsgOutline((WebMsgDeployLedStop) message);

            case MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_RESPONSE:
                WebMsgInitMarchConfirmCardResponse marchConfirmCard = (WebMsgInitMarchConfirmCardResponse) message;
                logger.info("生成初始化帧：匹配确认卡号状态：" + marchConfirmCard.isSuccessful());
                return tcpMsgBuilder.buildInitMarchConfirmCardSuccessful(marchConfirmCard);

            case MsgType.INITIALIZE_SERVER_CLEAR_INITIALIZE_MESSAGE:
                WebMsgInitClearDeviceStatus clearDeviceStatus = (WebMsgInitClearDeviceStatus) message;
                logger.info("生成帧：清除设备的初始化状态");
                return tcpMsgBuilder.buildClearDeviceStatus(clearDeviceStatus);

            default:
                logger.info("未匹配功能位「" + message.getMsgId() + "」，无法生成 CAN 帧");
                break;
        }
        return new byte[0];
    }
}
