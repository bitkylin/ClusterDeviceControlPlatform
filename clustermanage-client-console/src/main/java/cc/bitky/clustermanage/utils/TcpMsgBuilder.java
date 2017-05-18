package cc.bitky.clustermanage.utils;

import java.nio.charset.Charset;

import cc.bitky.clustermanage.netty.message.base.BaseMessage;
import cc.bitky.clustermanage.netty.message.base.BaseMsgCardNum;
import cc.bitky.clustermanage.netty.message.base.BaseTcpResponseMsg;

public class TcpMsgBuilder {
    Charset charset_GB2312 = Charset.forName("EUC-CN");

    /**
     * 「初始化」构建卡号的 CAN 帧，包括员工卡号和确认卡号
     *
     * @param msgCardNum 卡号 bean
     * @return 卡号的 CAN 帧
     */
    public byte[] buildInitConfirmCardNumber(BaseMsgCardNum msgCardNum) {
        byte[] bytes = buildMsgOutline(msgCardNum);
        bytes[0] += 8;
        byte[] byteCardNum = longToByteArray(msgCardNum.getCardNumber());
        addMessageBody(bytes, byteCardNum, 0);
        return bytes;
    }

    /**
     * 构建设备状态 CAN 帧
     *
     * @param responseMsg 设备状态 bean
     * @return 设备状态的 CAN 帧
     */
    public byte[] buildResponseMsg(BaseTcpResponseMsg responseMsg) {
        byte[] bytes = buildMsgOutline(responseMsg);
        bytes[0] += 1;
        bytes[5] = (byte) responseMsg.getStatus();
        return bytes;
    }

    /**
     * 构建CAN帧的轮廓，只需再填入数据位即可
     *
     * @param message 欲构建为CAN帧的bean
     * @return 轮廓CAN帧
     */
    private byte[] buildMsgOutline(BaseMessage message) {
        byte[] bytes = new byte[13];
        bytes[0] = (byte) 0x80;
        bytes[2] = (byte) message.getMsgId();
        bytes[3] = (byte) message.getBoxId();
        bytes[4] = (byte) message.getGroupId();
        return bytes;
    }

    /**
     * 在轮廓CAN帧中添加数据位
     *
     * @param bytes     轮廓CAN帧
     * @param bytesBody 数据位
     * @param offset    数据位的偏移量，offset位开始操作8个字节
     * @return 已构建完成的CAN帧
     */
    private byte[] addMessageBody(byte[] bytes, byte[] bytesBody, int offset) {
        int max = (bytesBody.length - offset) > 8 ? 8 : (bytesBody.length - offset);
        for (int i = 0; i < max; i++) {
            bytes[i + 5] = bytesBody[i + offset];
        }
        return bytes;
    }

    private long byteArrayToLong(byte[] bytes) {
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
