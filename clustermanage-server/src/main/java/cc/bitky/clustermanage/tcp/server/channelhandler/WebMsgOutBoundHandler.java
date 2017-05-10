package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.send.SendableMsg;
import cc.bitky.clustermanage.server.message.send.WebMsgGrouped;
import cc.bitky.clustermanage.server.message.send.WebMsgSpecial;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployFreeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployRemainChargeTimes;
import cc.bitky.clustermanage.server.message.web.WebMsgInitMarchConfirmCardSuccessful;
import cc.bitky.clustermanage.server.message.web.WebMsgObtainDeviceStatus;
import cc.bitky.clustermanage.server.message.web.WebMsgOperateBoxUnlock;
import cc.bitky.clustermanage.tcp.util.TcpMsgBuilder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

@Service
@ChannelHandler.Sharable
public class WebMsgOutBoundHandler extends ChannelOutboundHandlerAdapter {

    private final TcpMsgBuilder tcpMsgBuilder;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public WebMsgOutBoundHandler(TcpMsgBuilder tcpMsgBuilder) {
        this.tcpMsgBuilder = tcpMsgBuilder;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        IMessage message = (IMessage) msg;
        logger.info("------------即将发送CAN帧集合------------");
        logger.info("发送CAN帧「" + message.getGroupId() + ", " + message.getBoxId() + ", " + message.getMsgId() + "」");
        switch (message.getMsgId()) {

            case MsgType.SERVER_SEND_GROUPED:
                logger.info("$$$$$$$$$$$$轮询单个设备组$$$$$$$$$$$$$$$");
                WebMsgGrouped groupMsg = (WebMsgGrouped) message;
                IMessage baseMsg = groupMsg.getMessage();
                for (int j = 1; j <= groupMsg.getMaxBoxId(); j++) {
                    baseMsg.setBoxId(j);
                    SingleBuildBytes(ctx, baseMsg);
                }
                break;

            case MsgType.SERVER_SEND_SPECIAL:
                WebMsgSpecial msgSpecial = (WebMsgSpecial) message;
                logger.info("$$$$$$$$$$$$发送特殊帧「urgent:" + msgSpecial.isUrgent() + "responsive:" + msgSpecial.isResponsive() + "」$$$$$$$$$$$$$$$");
                IMessage baseMsgSpecial = msgSpecial.getMessage();
                deployWriteTcpSpecial(ctx, msgSpecial);
                break;

            default:
                SingleBuildBytes(ctx, message);
                break;
        }
//                case GROUP:
//                    logger.info("$$$$$$$$$$$$轮询集群切面方式$$$$$$$$$$$$$$$");
//                    for (int i = 1; i <= groupMsg.getMaxGroupId(); i++) {
//                        baseMsg.setGroupId(i);
//                        SingleBuildBytes(ctx, baseMsg);
//                    }
//                    break;
//                case ALL:
//                    logger.info("$$$$$$$$$$$$$轮询整个集群$$$$$$$$$$$$$$$$");
//                    for (int j = 1; j <= groupMsg.getMaxBoxId(); j++) {
//                        for (int i = 1; i <= groupMsg.getMaxGroupId(); i++) {
//                            baseMsg.setBoxId(j);
//                            baseMsg.setGroupId(i);
//                            SingleBuildBytes(ctx, baseMsg);
//                        }
//                    }
//                    break;
    }

    /**
     * 将非成组的 Message 转化为 byte[]
     *
     * @param ctx     上下文
     * @param message 信息 bean
     */
    private void SingleBuildBytes(ChannelHandlerContext ctx, IMessage message) {
        switch (message.getMsgId()) {
            case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_1:
                byte[] bytesDepartment = buildByMessage(message);
                byte[] bytesD1 = new byte[13];
                byte[] bytesD2 = new byte[13];
                System.arraycopy(bytesDepartment, 0, bytesD1, 0, 13);
                System.arraycopy(bytesDepartment, 13, bytesD2, 0, 13);
                deployWriteTcp(ctx, bytesD1);
                deployWriteTcp(ctx, bytesD2);
                return;

            case MsgType.SERVER_SET_FREE_CARD_NUMBER:
                byte[] bytesFree = buildByMessage(message);
                int count = bytesFree.length / 13;
                for (int i = 0; i < count; i++) {
                    byte[] bytesF = new byte[13];
                    System.arraycopy(bytesFree, 13 * i, bytesF, 0, 13);
                    deployWriteTcp(ctx, bytesF);
                }
                return;
        }
        deployWriteTcp(ctx, buildByMessage(message));
    }

    /**
     * 将 byte[] 转化为 SendableMsg 并传入下一级写出通道
     *
     * @param ctx   上下文
     * @param bytes 欲写入的 byte[]
     */
    private void deployWriteTcp(ChannelHandlerContext ctx, byte[] bytes) {
        ctx.write(new SendableMsg(bytes));
    }

    /**
     * 「特殊的的」将 byte[] 转化为 SendableMsg 并传入下一级写出通道
     *
     * @param ctx        上下文
     * @param msgSpecial 特殊 Message
     */
    private void deployWriteTcpSpecial(ChannelHandlerContext ctx, WebMsgSpecial msgSpecial) {

        SendableMsg sendableMsg = new SendableMsg(buildByMessage(msgSpecial.getMessage()));
        sendableMsg.setUrgent(msgSpecial.isUrgent());
        sendableMsg.setResponsive(msgSpecial.isResponsive());
        ctx.write(sendableMsg);
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

            case MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_SUCCESSFUL:
                WebMsgInitMarchConfirmCardSuccessful marchConfirmCard = (WebMsgInitMarchConfirmCardSuccessful) message;
                logger.info("生成初始化帧：匹配确认卡号状态：" + marchConfirmCard.isSuccessful());
                return tcpMsgBuilder.buildInitMarchConfirmCardSuccessful(marchConfirmCard);
        }
        return new byte[0];
    }
}
