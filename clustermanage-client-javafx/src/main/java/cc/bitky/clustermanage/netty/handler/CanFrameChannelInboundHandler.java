package cc.bitky.clustermanage.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeDepartment2;
import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployFreeCardSpecial;
import cc.bitky.clustermanage.netty.message.base.IMessage;
import cc.bitky.clustermanage.netty.message.tcp.MsgErrorMessage;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployRemainChargeTimes;
import cc.bitky.clustermanage.netty.message.web.WebMsgInitClearDeviceStatus;
import cc.bitky.clustermanage.netty.message.web.WebMsgInitMarchConfirmCardResponse;
import cc.bitky.clustermanage.netty.message.web.WebMsgObtainDeviceStatus;
import cc.bitky.clustermanage.netty.message.web.WebMsgOperateBoxUnlock;
import cc.bitky.clustermanage.utils.TcpMsgBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class CanFrameChannelInboundHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private static final int frameHead = 0x80;
    private Charset charset_GB2312 = Charset.forName("EUC-CN");
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private int errorCount = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        if (errorCount != 0) logger.warn("接收到错误的 CAN 帧数量：" + errorCount);
        if (msg.readableBytes() % 13 != 0) {
            errorCount++;
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
            String freeCardNumber = TcpMsgBuilder.byteArrayToString(bytes);
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
                String cardNumber = TcpMsgBuilder.byteArrayToString(bytes);
                return new WebMsgDeployEmployeeCardNumber(groupId, boxId, cardNumber);
            //服务器远程开锁
            case MsgType.SERVER_REMOTE_UNLOCK:
                msg.skipBytes(8);
                return new WebMsgOperateBoxUnlock(groupId, boxId);
            //服务器初始化: 匹配确认卡号状态
            case MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_RESPONSE:
                int MarchConfirmCardstatus = msg.readByte();
                msg.skipBytes(7);
                if (MarchConfirmCardstatus == 1) {
                    return new WebMsgInitMarchConfirmCardResponse(groupId, boxId, true);
                } else {
                    return new WebMsgInitMarchConfirmCardResponse(groupId, boxId, false);
                }
                //服务器初始化: 清除设备的初始化状态
            case MsgType.INITIALIZE_SERVER_CLEAR_INITIALIZE_MESSAGE:
                msg.skipBytes(8);
                return new WebMsgInitClearDeviceStatus(groupId, boxId);
            default:
                logger.warn("接收到正确的 CAN 帧，但无法解析");
                errorCount++;
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
}
