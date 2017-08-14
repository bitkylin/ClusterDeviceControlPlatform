package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.tcp.MsgErrorMessage;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgInitResponseCardNumber;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseDeviceId;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseEmployeeCardnumber;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseEmployeeDepartment1;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseEmployeeDepartment2;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseEmployeeName;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseFreeCardNumber;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseOperateBoxUnlock;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseRemainChargeTimes;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseStatus;
import cc.bitky.clustermanage.tcp.util.TcpMsgBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Component
@ChannelHandler.Sharable
public class CanFrameChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    int index = 0;
    byte b = (byte) 0x80;
    int a = 0xf0 & b;
    private byte[] bytes = new byte[13];

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {

        int remainder = msg.readableBytes() % 13;

        if (remainder == 0 && (msg.getByte(0) & 0xF0) == 0x80 & msg.getByte(1) == 0x00) {
            processByteBuf(ctx, msg);
            return;
        }

        if (remainder != 0 && index == 0 && (msg.getByte(0) & 0xF0) == 0x80 & msg.getByte(1) == 0x00) {
            logger.warn("读取到非整数个 CAN 帧");
            processByteBuf(ctx, msg);
            cacheRemainByte(msg);
            return;
        }

        if (index != 0) {
            if (cacheRemainByte(msg)) {
                ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
                if ((byteBuf.getByte(0) & 0xF0) == 0x80 & byteBuf.getByte(1) == 0x00) {
                    processByteBuf(ctx, byteBuf);
                }
            }
            remainder = msg.readableBytes() % 13;
            if (remainder == 0) {
                System.out.println("lml");
            }
            if (remainder == 0 && (msg.getByte(msg.readerIndex()) & 0xF0) == 0x80 & msg.getByte(msg.readerIndex() + 1) == 0x00) {
                processByteBuf(ctx, msg);
                return;
            }
            return;
        }
    }

    /**
     * 缓存可读数据中的非完整帧，拼接为完整帧后，执行正常操作
     *
     * @param msg 可读数据
     * @return 缓存为完整帧
     */
    private boolean cacheRemainByte(ByteBuf msg) {
        while (msg.isReadable()) {
            bytes[index] = msg.readByte();
            index++;
            if (index >= 13) {
                index = 0;
                return true;
            }
        }
        return false;
    }

    /**
     * 处理接收到的数据，转化为 Message 并送入下一级处理器
     *
     * @param ctx 处理器上下文
     * @param msg 待处理的已接收数据
     */

    private void processByteBuf(ChannelHandlerContext ctx, ByteBuf msg) {
        while (msg.readableBytes() >= 13) {
            msg.skipBytes(2);
            int msgId = msg.readByte();
            int deviceId = msg.readByte();
            int groupId = msg.readByte();
            IMessage message = handleMessage(msgId, deviceId, groupId, msg);
            ctx.fireChannelRead(message);
        }
    }

    private IMessage handleMessage(int msgId, int deviceId, int groupId, ByteBuf msg) {
        if (msgId >= 0x40 && msgId <= 0x4F) {
            int status = msg.readByte();
            msg.skipBytes(7);
            switch (msgId) {
                //设备状态帧回复
                case MsgType.DEVICE_RESPONSE_STATUS:
                    return new TcpMsgResponseStatus(groupId, deviceId, status);

                case MsgType.DEVICE_RESPONSE_REMAIN_CHARGE_TIMES:
                    return new TcpMsgResponseRemainChargeTimes(groupId, deviceId, status);

                case MsgType.DEVICE_RESPONSE_DEVICE_ID:
                    return new TcpMsgResponseDeviceId(groupId, deviceId, status);

                case MsgType.DEVICE_RESPONSE_EMPLOYEE_NAME:
                    return new TcpMsgResponseEmployeeName(groupId, deviceId, status);

                case MsgType.DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_1:
                    return new TcpMsgResponseEmployeeDepartment1(groupId, deviceId, status);

                case MsgType.DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_2:
                    return new TcpMsgResponseEmployeeDepartment2(groupId, deviceId, status);

                case MsgType.DEVICE_RESPONSE_EMPLOYEE_CARD_NUMBER:
                    return new TcpMsgResponseEmployeeCardnumber(groupId, deviceId, status);

                case MsgType.DEVICE_RESPONSE_REMOTE_UNLOCK:
                    return new TcpMsgResponseOperateBoxUnlock(groupId, deviceId, status);
            }
        }


        if (msgId >= ((byte) 0x80) && msgId <= ((byte) 0x8F)) {
            int status = msg.readByte();
            msg.skipBytes(7);
            return new TcpMsgResponseFreeCardNumber(groupId, deviceId, msgId, status);
        }

        switch (msgId) {

            //「初始化」回复卡号
            case MsgType.INITIALIZE_DEVICE_RESPONSE_CARD:
                byte[] cardArray = new byte[8];
                msg.readBytes(cardArray);
                String cardNum = TcpMsgBuilder.byteArrayToString(cardArray);
                return new TcpMsgInitResponseCardNumber(groupId, deviceId, cardNum);

            default:
                String exMsg = "接收到正确的 CAN 帧，但无法解析「msgId = " + msgId + "」『发送卡号 0 的帧』";
                logger.warn(exMsg);
                return new MsgErrorMessage(exMsg);
        }
    }
}
