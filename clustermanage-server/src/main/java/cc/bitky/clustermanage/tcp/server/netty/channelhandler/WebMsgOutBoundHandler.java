package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import cc.bitky.clustermanage.server.MsgType;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgOperateBoxUnlock;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
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
    Logger logger = LoggerFactory.getLogger(WebMsgOutBoundHandler.class);

    @Autowired
    public WebMsgOutBoundHandler(TcpMsgBuilder tcpMsgBuilder) {
        this.tcpMsgBuilder = tcpMsgBuilder;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        List<IMessage> messages = (List<IMessage>) msg;
        logger.info("-----------即将发送CAN帧集合----------");

        messages.forEach(message -> {
            logger.info("发送CAN帧: " + "group-" + message.getGroupId() + ",box-" + message.getBoxId());
            switch (message.getMsgId()) {
                case MsgType.SERVER_SET_EMPLOYEE_NAME:
                    logger.info("设置员工姓名");
                    ctx.write(Unpooled.wrappedBuffer(
                            tcpMsgBuilder.buildEmployeeName((WebMsgDeployEmployeeName) message)));
                    break;
                case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_1:
                    logger.info("设置员工单位");
                    ctx.write(Unpooled.wrappedBuffer(
                            tcpMsgBuilder.buildEmployeeDepartment((WebMsgDeployEmployeeDepartment) message)));
                    break;
                case MsgType.SERVER_SET_EMPLOYEE_CARD_NUMBER:
                    logger.info("设置员工卡号");
                    ctx.write(Unpooled.wrappedBuffer(
                            tcpMsgBuilder.buildEmployeeCardNumber((WebMsgDeployEmployeeCardNumber) message)));
                    break;
                case MsgType.SERVER_REMOTE_UNLOCK:
                    logger.info("解锁单个充电柜");
                    ctx.write(Unpooled.wrappedBuffer(
                            tcpMsgBuilder.buildWebUnlock((WebMsgOperateBoxUnlock) message)));
                    break;
            }
        });
        ctx.flush();
    }
}
