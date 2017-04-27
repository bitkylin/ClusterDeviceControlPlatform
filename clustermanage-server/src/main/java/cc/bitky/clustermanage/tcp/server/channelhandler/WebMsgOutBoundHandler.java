package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployFreeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployRemainChargeTimes;
import cc.bitky.clustermanage.server.message.web.WebMsgGrouped;
import cc.bitky.clustermanage.server.message.web.WebMsgInitCardException;
import cc.bitky.clustermanage.server.message.web.WebMsgInitDeployMessageComplete;
import cc.bitky.clustermanage.server.message.web.WebMsgInitMarchComfirmCardSuccessful;
import cc.bitky.clustermanage.server.message.web.WebMsgObtainDeviceStatus;
import cc.bitky.clustermanage.server.message.web.WebMsgOperateBoxUnlock;
import cc.bitky.clustermanage.tcp.util.TcpMsgBuilder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

@Service
@ChannelHandler.Sharable
public class WebMsgOutBoundHandler extends ChannelOutboundHandlerAdapter {

    private final TcpMsgBuilder tcpMsgBuilder;
    private Logger logger = LoggerFactory.getLogger(WebMsgOutBoundHandler.class);

    @Autowired
    public WebMsgOutBoundHandler(TcpMsgBuilder tcpMsgBuilder) {
        this.tcpMsgBuilder = tcpMsgBuilder;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        List<IMessage> messages = (List<IMessage>) msg;
        logger.info("------------即将发送CAN帧集合------------");
        messages.forEach(message -> {
            logger.info("发送CAN帧「" + message.getGroupId() + ", " + message.getBoxId() + ", " + message.getMsgId() + "」");
            if (message.getMsgId() != MsgType.SERVER_SEND_GROUPED) {
                ctx.write(Unpooled.wrappedBuffer(buildByMessage(message)));
            } else {
                WebMsgGrouped groupMsg = (WebMsgGrouped) message;
                IMessage baseMsg = groupMsg.getMessage();

                switch (groupMsg.getGroupedEnum()) {
                    case BOX:
                        logger.info("$$$$$$$$$$$$轮询单个设备组$$$$$$$$$$$$$$$");
                        for (int j = 1; j <= groupMsg.getMaxBoxId(); j++) {
                            baseMsg.setBoxId(j);
                            ctx.write(Unpooled.wrappedBuffer(buildByMessage(baseMsg)));
                        }
                        break;
                    case ALL:
                        logger.info("$$$$$$$$$$$$轮询整个集群$$$$$$$$$$$$$$$");
                        for (int j = 1; j <= groupMsg.getMaxBoxId(); j++) {
                            for (int i = 1; i <= groupMsg.getMaxGroupId(); i++) {
                                baseMsg.setBoxId(j);
                                baseMsg.setGroupId(i);
                                ctx.write(Unpooled.wrappedBuffer(buildByMessage(baseMsg)));
                            }
                        }
                        break;
                }
            }
        });
        ctx.flush();
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

            case MsgType.INITIALIZE_SERVER_DEPLOY_MESSAGE_COMPLETE:
                logger.info("生成初始化帧：下发初始化信息完成");
                return tcpMsgBuilder.buildInitDeployMsgComplete((WebMsgInitDeployMessageComplete) message);

            case MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_SUCCESSFUL:
                logger.info("生成初始化帧：匹配确认卡号成功");
                return tcpMsgBuilder.buildInitMarchConfirmCardSuccessful((WebMsgInitMarchComfirmCardSuccessful) message);

            case MsgType.INITIALIZE_SERVER_MARCH_CARD_EXCEPTION:
                logger.info("生成初始化帧：匹配卡号失败");
                return tcpMsgBuilder.buildInitMarchCardException((WebMsgInitCardException) message);
        }
        return new byte[0];
    }
}
