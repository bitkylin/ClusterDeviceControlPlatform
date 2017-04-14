package cc.bitky.clustermanage.tcp.util;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

import cc.bitky.clustermanage.server.message.BaseMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgOperateBoxUnlock;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;

@Component
public class TcpMsgBuilder {
    Charset charset_GB2312 = Charset.forName("EUC-CN");

    /**
     * 构建员工姓名的CAN帧
     *
     * @param webMsgDeployEmployeeName 员工姓名bean
     * @return 员工姓名的CAN帧
     */
    public byte[] buildEmployeeName(WebMsgDeployEmployeeName webMsgDeployEmployeeName) {
        byte[] bytes = buildMsgOutline(webMsgDeployEmployeeName);
        bytes[0] += 2 * webMsgDeployEmployeeName.getValue().length();
        byte[] bytesBody = webMsgDeployEmployeeName.getValue().getBytes(charset_GB2312);
        addMessageBody(bytes, bytesBody, 0);
        return bytes;
    }

    /**
     * 构建员工卡号的CAN帧
     *
     * @param webMsgDeployEmployeeCardNumber 员工卡号bean
     * @return 员工卡号的CAN帧
     */
    public byte[] buildEmployeeCardNumber(WebMsgDeployEmployeeCardNumber webMsgDeployEmployeeCardNumber) {
        byte[] bytes = buildMsgOutline(webMsgDeployEmployeeCardNumber);
        bytes[0] += 8;
        byte[] byteCardNum = longToByteArray(webMsgDeployEmployeeCardNumber.getCardNumber());
        addMessageBody(bytes, byteCardNum, 0);
        return bytes;
    }

    /**
     * 构建员工单位的CAN帧
     *
     * @param webMsgDeployEmployeeDepartment 员工单位bean
     * @return 员工单位的CAN帧
     */
    public byte[] buildEmployeeDepartment(WebMsgDeployEmployeeDepartment webMsgDeployEmployeeDepartment) {
        byte[] bytes = buildMsgOutline(webMsgDeployEmployeeDepartment);
        byte[] bytesBody = webMsgDeployEmployeeDepartment.getValue().getBytes(charset_GB2312);

        byte[] bytes2 = bytes.clone();
        bytes2[2] += 1;
        addMessageBody(bytes, bytesBody, 0);
        addMessageBody(bytes2, bytesBody, 8);

        if (bytesBody.length > 8) {
            bytes[0] += 8;
            bytes2[0] += (bytesBody.length - 8) > 8 ? 8 : (bytesBody.length - 8);
        } else {
            bytes[0] += bytesBody.length;
        }

        byte[] bytes3 = new byte[26];
        System.arraycopy(bytes, 0, bytes3, 0, 13);
        System.arraycopy(bytes2, 0, bytes3, 13, 13);
        return bytes3;
    }

    /**
     * 构建开锁用的CAN帧
     *
     * @param webMsgOperateBoxUnlock 开锁bean
     * @return 员工单位的CAN帧
     */
    public byte[] buildWebUnlock(WebMsgOperateBoxUnlock webMsgOperateBoxUnlock) {
        return buildMsgOutline(webMsgOperateBoxUnlock);
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

    public long byteArrayToLong(byte[] bytes) {
        long num = 0;
        for (int i = 0; i <= 7; i++) {
            num += (bytes[i] & 0xffL) << ((7 - i) * 8);
        }
        return num;
    }

    public byte[] longToByteArray(long num) {
        byte[] bytes = new byte[8];
        for (int i = 0; i <= 7; i++) {
            bytes[i] = (byte) ((num >> ((7 - i) * 8)) & 0xff);
        }
        return bytes;
    }


}
