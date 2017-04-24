package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployFreeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgGrouped;
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
            logger.info("发送CAN帧「" + message.getGroupId() + ", " + message.getBoxId() + "」");
            if (message.getMsgId() != MsgType.SERVER_SEND_GROUPED)
                ctx.write(Unpooled.wrappedBuffer(buildByMessage(message)));
            else {
                WebMsgGrouped groupMsg = (WebMsgGrouped) message;
                IMessage baseMsg = groupMsg.getMessage();

                switch (groupMsg.getGroupedEnum()) {
                    case BOX:
                        for (int j = 1; j <= groupMsg.getMaxBoxId(); j++) {
                            baseMsg.setBoxId(j);
                            ctx.write(Unpooled.wrappedBuffer(buildByMessage(baseMsg)));
                        }
                        break;
                    case ALL:
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
            case MsgType.SERVER_SET_DEVICE_ID:
                logger.info("生成帧：设置设备 ID");
                return tcpMsgBuilder.buildEmployeeName((WebMsgDeployEmployeeName) message);

            case MsgType.SERVER_SET_EMPLOYEE_NAME:
                logger.info("生成帧：设置员工姓名");
                return tcpMsgBuilder.buildEmployeeName((WebMsgDeployEmployeeName) message);

            case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_1:
                logger.info("生成帧：设置员工单位");
                return tcpMsgBuilder.buildEmployeeDepartment((WebMsgDeployEmployeeDepartment) message);

            case MsgType.SERVER_SET_EMPLOYEE_CARD_NUMBER:
                logger.info("生成帧：设置员工卡号");
                return tcpMsgBuilder.buildEmployeeCardNumber((WebMsgDeployEmployeeCardNumber) message);

            case MsgType.SERVER_REMOTE_UNLOCK:
                logger.info("生成帧：解锁单个设备");
                return tcpMsgBuilder.buildWebUnlock((WebMsgOperateBoxUnlock) message);

            case MsgType.SERVER_SET_FREE_CARD_NUMBER:
                logger.info("生成帧：设置万能卡号");
                return tcpMsgBuilder.buildFreeCardNumber((WebMsgDeployFreeCardNumber) message);
        }
        return new byte[0];
    }
}
