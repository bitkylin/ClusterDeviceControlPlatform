package cc.bitky.clustermanage.server.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import cc.bitky.clustermanage.db.bean.Employee;
import cc.bitky.clustermanage.db.presenter.KyDbPresenter;
import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgInitEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseDeviceStatus;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.server.message.web.WebMsgInitDeployMessageComplete;
import cc.bitky.clustermanage.tcp.server.netty.SendWebMessagesListener;

@Service
public class ServerTcpMessageHandler {
    private final KyDbPresenter kyDbPresenter;
    private Logger logger = LoggerFactory.getLogger(ServerTcpMessageHandler.class);
    private SendWebMessagesListener sendWebMessagesListener;
    private KyServerCenterHandler kyServerCenterHandler;

    @Autowired
    public ServerTcpMessageHandler(KyDbPresenter kyDbPresenter) {
        this.kyDbPresenter = kyDbPresenter;

    }

    /**
     * 功能消息处理方法
     *
     * @param message 功能消息 bean
     */
    public void handleTcpMsg(IMessage message) {
        logger.info("***********进入功能消息处理方法「" + message.getGroupId() + ", " + message.getBoxId() + "」***********");
        int msgId = message.getMsgId();
        switch (msgId) {
            case MsgType.DEVICE_RESPONSE_STATUS:
                logger.info("收到：设备状态");
                long l1 = System.currentTimeMillis();
                kyDbPresenter.handleMsgDeviceStatus((TcpMsgResponseDeviceStatus) message, true);
                long l2 = System.currentTimeMillis();
                System.out.println("处理设备状态包花费的总时间：" + (l2 - l1) + "ms");
                break;
        }
    }

    /**
     * 常规回复消息处理方法
     *
     * @param message 常规回复消息 bean
     */
    public void handleTcpResponseMsg(IMessage message) {
        logger.info("***********进入常规回复消息处理方法「" + message.getGroupId() + ", " + message.getBoxId() + "」***********");
        int msgId = message.getMsgId();
        switch (msgId) {
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
                logger.info("收到：单位更新回复");
                break;
            case MsgType.DEVICE_RESPONSE_EMPLOYEE_CARD_NUMBER:
                logger.info("收到：卡号更新回复");
                break;
            case MsgType.DEVICE_RESPONSE_REMOTE_UNLOCK:
                logger.info("收到：远程开锁回复");
                break;
            case MsgType.DEVICE_RESPONSE_FREE_CARD_NUMBER:
                logger.info("收到：万能卡号更新回复");
                break;
        }
    }

    /**
     * 初始化消息处理方法
     *
     * @param message 初始化消息 bean
     */
    public void handleTcpInitMsg(IMessage message) {
        logger.info("***********进入初始化消息处理方法「" + message.getGroupId() + ", " + message.getBoxId() + "」***********");
        switch (message.getMsgId()) {
            //从数据库中获取员工卡号对应的信息并发送至 Netty 的 Handler
            case MsgType.INITIALIZE_DEVICE_RESPONSE_EMPLOYEE_CARD:
                TcpMsgInitEmployeeCardNumber tcpMsgInitEmployeeCardNumber = (TcpMsgInitEmployeeCardNumber) message;
                Employee employee = kyDbPresenter.obtainDeviceByEmployeeCard(tcpMsgInitEmployeeCardNumber.getCardNumber());
                List<IMessage> messages = new ArrayList<>();
                if (employee.getGroupId() == 0 || employee.getGroupId() != message.getGroupId()) {
                    employee.setGroupId(message.getGroupId());
                    employee.setDeviceId(0xFE);
                }
                messages.add(new WebMsgDeployEmployeeDeviceId(message.getGroupId(), message.getBoxId(), employee.getDeviceId()));
                messages.add(new WebMsgDeployEmployeeName(message.getGroupId(), message.getBoxId(), employee.getName()));
                messages.add(new WebMsgDeployEmployeeDepartment(message.getGroupId(), message.getBoxId(), employee.getDepartment()));
                sendMsgToTcp(messages);
                kyServerCenterHandler.deployFreeCard(message.getGroupId(), message.getBoxId());
                messages = new ArrayList<>();
                messages.add(new WebMsgInitDeployMessageComplete(message.getGroupId(), message.getBoxId()));
                sendMsgToTcp(messages);
                break;

            case MsgType.INITIALIZE_DEVICE_RESPONSE_CONFIRM_CARD:

                break;
        }
    }

    public void setSendWebMessagesListener(SendWebMessagesListener sendWebMessagesListener) {
        this.sendWebMessagesListener = sendWebMessagesListener;
    }

    boolean sendMsgToTcp(List<IMessage> messages) {
        if (sendWebMessagesListener == null) {
            logger.warn("Server 模块未能与 Netty 模块建立连接，故不能发送消息集合");
            return false;
        }
        return sendWebMessagesListener.sendMessagesToTcp(messages);
    }

    void setKyServerCenterHandler(KyServerCenterHandler kyServerCenterHandler) {
        this.kyServerCenterHandler = kyServerCenterHandler;
    }
}
