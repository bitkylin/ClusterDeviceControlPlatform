package cc.bitky.clustermanage.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

import cc.bitky.clustermanage.netty.WebMsgDeployEmployeeDepartment2;
import cc.bitky.clustermanage.netty.message.CardType;
import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.WebMsgDeployFreeCardSpecial;
import cc.bitky.clustermanage.netty.message.base.IMessage;
import cc.bitky.clustermanage.netty.message.tcp.MsgErrorMessage;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployRemainChargeTimes;
import cc.bitky.clustermanage.netty.message.web.WebMsgInitCardException;
import cc.bitky.clustermanage.netty.message.web.WebMsgInitDeployMessageComplete;
import cc.bitky.clustermanage.netty.message.web.WebMsgInitMarchComfirmCardSuccessful;
import cc.bitky.clustermanage.netty.message.web.WebMsgObtainDeviceStatus;
import cc.bitky.clustermanage.netty.message.web.WebMsgOperateBoxUnlock;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CanFrameChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final int frameHead = 0x80;
    private Charset charset_GB2312 = Charset.forName("EUC-CN");
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        if (msg.readableBytes() % 13 != 0) {
            logger.warn("读取到非整数个 CAN 帧");
            return;
        }

        while (msg.readableBytes() >= 13) {
            int bodyLength = msg.readByte() & 0x0F;
            msg.skipBytes(1);
            int msgId = msg.readByte();
            int boxId = msg.readByte();
            int groupId = msg.readByte();
            IMessage message = handleMessage(msgId, groupId, boxId, bodyLength, msg);
            ctx.fireChannelRead(message);
        }
    }

    /**
     * 根据CAN帧生成相应的 Java bean
     *
     * @param msgId      CAN帧功能位
     * @param groupId    设备组 Id
     * @param boxId      设备 Id
     * @param bodyLength 数据部分长度
     * @param msg        数据部分 buffer
     * @return 生成的消息 bean
     */
    private IMessage handleMessage(int msgId, int groupId, int boxId, int bodyLength, ByteBuf msg) {

        //服务器部署万能卡号
        if (msgId >= 0x70 && msgId <= 0x7F) {
            byte[] bytes = new byte[8];
            msg.readBytes(bytes);
            long freeCardNumber = byteArrayToLong(bytes);
            return new WebMsgDeployFreeCardSpecial(groupId, boxId, freeCardNumber, msgId - 0x70);
        }

        switch (msgId) {
            //服务器请求设备的状态
            case MsgType.SERVER_REQUSET_STATUS:
                msg.skipBytes(8);
                return new WebMsgObtainDeviceStatus(groupId, boxId);
            //服务器部署剩余充电次数
            case MsgType.SERVER_SET_REMAIN_CHARGE_TIMES:
                int times = msg.readByte();
                msg.skipBytes(7);
                return new WebMsgDeployRemainChargeTimes(groupId, boxId, times);
            //服务器部署设备 Id 更新
            case MsgType.SERVER_SET_DEVICE_ID:
                int newBoxId = msg.readByte();
                msg.skipBytes(7);
                return new WebMsgDeployEmployeeDeviceId(groupId, boxId, newBoxId);
            //服务器部署员工姓名更新
            case MsgType.SERVER_SET_EMPLOYEE_NAME:
                String name = readGB2312Body(msg).substring(0, bodyLength / 2);
                return new WebMsgDeployEmployeeName(groupId, boxId, name);
            //服务器部署员工单位「1」更新
            case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_1:
                String department1 = readGB2312Body(msg).substring(0, bodyLength / 2);
                return new WebMsgDeployEmployeeDepartment(groupId, boxId, department1);
            //服务器部署员工单位「2」更新
            case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_2:
                String department2 = readGB2312Body(msg).substring(0, bodyLength / 2);
                return new WebMsgDeployEmployeeDepartment2(groupId, boxId, department2);
            //服务器部署员工卡号更新
            case MsgType.SERVER_SET_EMPLOYEE_CARD_NUMBER:
                byte[] bytes = new byte[8];
                msg.readBytes(bytes);
                long cardNumber = byteArrayToLong(bytes);
                return new WebMsgDeployEmployeeCardNumber(groupId, boxId, cardNumber);
            //服务器远程开锁
            case MsgType.SERVER_REMOTE_UNLOCK:
                msg.skipBytes(8);
                return new WebMsgOperateBoxUnlock(groupId, boxId);
            //服务器初始化: 信息发送完毕
            case MsgType.INITIALIZE_SERVER_DEPLOY_MESSAGE_COMPLETE:
                msg.skipBytes(8);
                return new WebMsgInitDeployMessageComplete(groupId, boxId);
            //服务器初始化: 匹配确认卡号成功
            case MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_SUCCESSFUL:
                msg.skipBytes(8);
                return new WebMsgInitMarchComfirmCardSuccessful(groupId, boxId);
            //服务器初始化: 匹配卡号失败
            case MsgType.INITIALIZE_SERVER_MARCH_CARD_EXCEPTION:
                msg.skipBytes(8);
                return new WebMsgInitCardException(groupId, boxId, CardType.EMPLOYEE);
            default:
                logger.warn("接收到正确的 CAN 帧，但无法解析");
                return new MsgErrorMessage("接收到正确的 CAN 帧，但无法解析");
        }
    }

    /**
     * 读取数据部分为字符串
     *
     * @param msg CAN帧 buffer
     * @return 读取的字符串
     */
    private String readGB2312Body(ByteBuf msg) {
        byte[] bytes = new byte[8];
        msg.readBytes(bytes);
        return new String(bytes, charset_GB2312);
    }

    long byteArrayToLong(byte[] bytes) {
        long num = 0;
        for (int i = 0; i <= 7; i++) {
            num += (bytes[i] & 0xffL) << ((7 - i) * 8);
        }
        return num;
    }

    private byte[] longToByteArray(long num) {
        byte[] bytes = new byte[8];
        for (int i = 0; i <= 7; i++) {
            bytes[i] = (byte) ((num >> ((7 - i) * 8)) & 0xff);
        }
        return bytes;
    }
}
