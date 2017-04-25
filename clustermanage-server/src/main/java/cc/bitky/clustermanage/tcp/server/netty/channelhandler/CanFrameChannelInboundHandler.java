package cc.bitky.clustermanage.tcp.server.netty.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.tcp.MsgErrorMessage;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgInitResponseCardNumber;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseBoxId;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseDeviceStatus;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseEmployeeDepartment;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseEmployeeName;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseFreeCardNumber;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseOperateBoxUnlock;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseRemainChargeTimes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
@ChannelHandler.Sharable
public class CanFrameChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        if (msg.readableBytes() % 13 != 0) {
            logger.warn("读取到非整数个 CAN 帧");
            return;
        }

        while (msg.readableBytes() >= 13) {
            msg.skipBytes(2);
            int msgId = msg.readByte();
            int boxId = msg.readByte();
            int groupId = msg.readByte();
            IMessage message = handleMessage(msgId, boxId, groupId, msg);
            ctx.fireChannelRead(message);
        }
    }

    private IMessage handleMessage(int msgId, int boxId, int groupId, ByteBuf msg) {
        if (msgId >= 0x40 && msgId <= 0x4F) {
            int status = msg.readByte();
            msg.skipBytes(7);

            switch (msgId) {
                //设备状态帧回复
                case MsgType.DEVICE_RESPONSE_STATUS:
                    return new TcpMsgResponseDeviceStatus(groupId, boxId, status);

                case MsgType.DEVICE_RESPONSE_REMAIN_CHARGE_TIMES:
                    return new TcpMsgResponseRemainChargeTimes(groupId, boxId, status);

                case MsgType.DEVICE_RESPONSE_DEVICE_ID:
                    return new TcpMsgResponseBoxId(groupId, boxId, status);

                case MsgType.DEVICE_RESPONSE_EMPLOYEE_NAME:
                    return new TcpMsgResponseEmployeeName(groupId, boxId, status);

                case MsgType.DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_1:
                    return new TcpMsgResponseEmployeeDepartment(groupId, boxId, status);

                case MsgType.DEVICE_RESPONSE_EMPLOYEE_CARD_NUMBER:
                    return new TcpMsgInitResponseCardNumber(groupId, boxId, status);

                case MsgType.DEVICE_RESPONSE_REMOTE_UNLOCK:
                    return new TcpMsgResponseOperateBoxUnlock(groupId, boxId, status);

                case MsgType.DEVICE_RESPONSE_FREE_CARD_NUMBER:
                    return new TcpMsgResponseFreeCardNumber(groupId, boxId, status);
            }
        }

        switch (msgId) {

            //「初始化」回复卡号
            case MsgType.INITIALIZE_DEVICE_RESPONSE_CARD:
                byte[] cardArray = new byte[8];
                msg.readBytes(cardArray);
                long cardNum = byteArrayToLong(cardArray);
                return new TcpMsgInitResponseCardNumber(groupId, boxId, cardNum);

            default:
                String exMsg = "接收到正确的 CAN 帧，但无法解析「msgId = " + msgId + "」";
                logger.warn(exMsg);
                return new MsgErrorMessage(exMsg);
        }
    }


    private long byteArrayToLong(byte[] bytes) {
        long num = 0;
        for (int i = 0; i <= 7; i++) {
            num += (bytes[i] & 0xffL) << ((7 - i) * 8);
        }
        return num;
    }
}
