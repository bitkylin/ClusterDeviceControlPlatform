package cc.bitky.clusterdeviceplatform.server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.define.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.msg.MsgReplyChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecDeviceRemainChargeTimes;
import cc.bitky.clusterdeviceplatform.server.config.CommSetting;
import cc.bitky.clusterdeviceplatform.server.db.DbPresenter;
import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.tcp.TcpPresenter;
import cc.bitky.clusterdeviceplatform.server.tcp.exception.ExceptionMsgTcp;
import cc.bitky.clusterdeviceplatform.server.tcp.repo.TcpRepository;

@Service
public class ServerTcpProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TcpRepository.class);
    private final TcpPresenter tcpPresenter;
    private final ServerCenterProcessor centerProcessor;
    private final DbPresenter dbPresenter;

    @Autowired
    public ServerTcpProcessor(TcpPresenter tcpPresenter, ServerCenterProcessor centerProcessor, DbPresenter dbPresenter) {
        this.tcpPresenter = tcpPresenter;
        this.centerProcessor = centerProcessor;
        this.dbPresenter = dbPresenter;
        tcpPresenter.setServer(this);
        centerProcessor.setTcpProcessor(this);
    }

    /**
     * 将指定的消息对象发送至 TCP 通道
     *
     * @param message 指定的消息对象
     */
    boolean sendMessage(BaseMsg message) {
        logger.info("发送消息：「" + message.getMsgDetail() + "」");
        return tcpPresenter.sendMessageToTcp(message);
    }

    /**
     * Netty 模块捕获到 Java 消息对象
     *
     * @param message 消息对象
     */
    public void huntMessage(BaseMsg message) {
        logger.info("捕获到「普通」消息对象：「" + message.getMsgDetail() + "」");
    }

    /**
     * Netty 模块捕获到「设备充电状态」消息对象
     *
     * @param message 消息对象
     */
    public void huntChargeStatusMsg(MsgReplyChargeStatus message) {
        logger.info("捕获到「充电状态」消息对象：「" + message.getMsgDetail() + "」");
        long l1 = System.currentTimeMillis();
        Device device = dbPresenter.handleMsgDeviceStatus(message);
        //部署剩余充电次数
        if (device != null) {
            deployRemainChargeTimes(device);
        }
        long l2 = System.currentTimeMillis();
        logger.info("处理「设备充电状态」总花费时间：" + (l2 - l1) + "ms");
    }

    /**
     * 部署剩余充电次数
     *
     * @param device 处理后的 Device
     */
    private void deployRemainChargeTimes(Device device) {
        //当前充电状态为「充满」，并且剩余充电次数小于或等于阈值时，部署剩余充电次数
        if (device.getStatus() == ChargeStatus.FULL && device.getRemainChargeTime() <= CommSetting.DEPLOY_REMAIN_CHARGE_TIMES) {
            int remainTimes = device.getRemainChargeTime();
            remainTimes = remainTimes > 0 ? remainTimes : 0;
            sendMessage(MsgCodecDeviceRemainChargeTimes.create(device.getGroupId(), device.getDeviceId(), remainTimes));
        }
    }

    /**
     * 「内部接口」发生异常时回调该接口传出异常信息
     *
     * @param msg 一场消息对象
     */
    public void touchUnusualMsg(ExceptionMsgTcp msg) {
        logger.info("捕获到异常消息：" + msg.getDetail() + "原始消息：「" + msg.getBaseMsg().getMsgDetail() + "」");
    }
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        Executors.newSingleThreadExecutor().submit(() -> {
//            System.out.println("请输入要发送的消息:");
//            Scanner scanner = new Scanner(System.in);
//            while (true) {
//                switch (scanner.nextInt()) {
//                    case 0:
//                        sendMessage(MsgCodecEmployeeName.create(1, 2, "李明亮"));
//                        break;
//                    case 1:
//                        // sendMessage(MsgCodecEmployeeName.create(1, 2, "李明亮"));
//                        sendMessage(MsgCodecEmployeeDepartment.create(1, 2, "李明亮部门"));
//                        break;
//                    case 2:
//                        sendMessage(MsgCodecEmployeeName.create(1, 2, "李明亮"));
//                        sendMessage(MsgCodecEmployeeDepartment.create(1, 2, "李明亮部门"));
//                        sendMessage(MsgCodecCardEmployee.create(1, 2, "12345"));
//                        break;
//                    case 3:
//                        sendMessage(MsgCodecEmployeeName.create(1, 4, "李明亮"));
//                        sendMessage(MsgCodecEmployeeDepartment.create(1, 5, "李明亮部门"));
//                        sendMessage(MsgCodecCardEmployee.create(1, 6, "12345"));
//                        break;
//                    case 4:
//                        sendMessage(MsgCodecEmployeeName.create(2, 2, "李明亮"));
//                        sendMessage(MsgCodecEmployeeDepartment.create(2, 2, "李明亮部门"));
//                        sendMessage(MsgCodecCardEmployee.create(2, 2, "12345"));
//                        break;
//                    case 5:
//                        sendMessage(MsgCodecEmployeeName.create(1, 2, "李明亮"));
//                        sendMessage(MsgCodecEmployeeDepartment.create(1, 2, "李明亮部门"));
//                        sendMessage(MsgCodecCardEmployee.create(1, 2, "12345"));
//
//                        sendMessage(MsgCodecEmployeeName.create(1, 4, "李明亮"));
//                        sendMessage(MsgCodecEmployeeDepartment.create(1, 5, "李明亮部门"));
//                        sendMessage(MsgCodecCardEmployee.create(1, 6, "12345"));
//
//                        sendMessage(MsgCodecEmployeeName.create(2, 2, "李明亮"));
//                        sendMessage(MsgCodecEmployeeDepartment.create(2, 2, "李明亮部门"));
//                        sendMessage(MsgCodecCardEmployee.create(2, 2, "12345"));
//                        break;
//                    default:
//                }
//            }
//        });
//
//    }
}
