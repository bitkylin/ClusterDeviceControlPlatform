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
        byte[] byteCardNum = stringToByteArray(msgCardNum.getCardNumber());
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
     */
    private void addMessageBody(byte[] bytes, byte[] bytesBody, int offset) {
        int max = (bytesBody.length - offset) > 8 ? 8 : (bytesBody.length - offset);
        for (int i = 0; i < max; i++) {
            bytes[i + 5] = bytesBody[i + offset];
        }
    }

    public static String byteArrayToString(byte[] cards) {
        StringBuilder builder = new StringBuilder();
        for (byte b : cards) {
            String s = Integer.toHexString(b & 0xFF).toUpperCase();
            if (s.length() == 1) {
                builder.append('0').append(s);
            } else builder.append(s);
        }
        return builder.toString();
    }

    public static byte[] stringToByteArray(String cards) {
        if (cards.length() > 16) cards = cards.substring(0, 16);
        if (cards.length() < 16) {
            int count = 16 - cards.length();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < count; i++) {
                builder.append("0");
            }
            builder.append(cards);
            cards = builder.toString();
        }

        byte[] bytes = new byte[8];
        cards = cards.toUpperCase();
        char[] hexChars = cards.toCharArray();
        for (int i = 0; i < 8; i++) {
            int pos = i * 2;
            bytes[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return bytes;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }



}
