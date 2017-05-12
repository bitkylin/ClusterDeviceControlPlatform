package cc.bitky.clustermanage.tcp.util;

import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

import cc.bitky.clustermanage.server.message.base.BaseMessage;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployFreeCardNumber;
import cc.bitky.clustermanage.server.message.web.WebMsgDeployRemainChargeTimes;
import cc.bitky.clustermanage.server.message.web.WebMsgInitClearDeviceStatus;
import cc.bitky.clustermanage.server.message.web.WebMsgInitMarchConfirmCardResponse;
import cc.bitky.clustermanage.server.message.web.WebMsgObtainDeviceStatus;
import cc.bitky.clustermanage.server.message.web.WebMsgOperateBoxUnlock;

@Component
public class TcpMsgBuilder {
    Charset charset_GB2312 = Charset.forName("EUC-CN");

    /**
     * 构建获取设备状态的 CAN 帧
     *
     * @param webMsgObtainDeviceStatus 获取设备状态 bean
     * @return 获取设备状态的 CAN 帧
     */
    public byte[] buildRequestDeviceStatus(WebMsgObtainDeviceStatus webMsgObtainDeviceStatus) {
        return buildMsgOutline(webMsgObtainDeviceStatus);
    }

    /**
     * 构建剩余充电次数的 CAN 帧
     *
     * @param webMsgDeployRemainChargeTimes 剩余充电次数 bean
     * @return 剩余充电次数的 CAN 帧
     */
    public byte[] buildRemainChargeTimes(WebMsgDeployRemainChargeTimes webMsgDeployRemainChargeTimes) {
        byte[] bytes = buildMsgOutline(webMsgDeployRemainChargeTimes);
        bytes[0] += 1;
        bytes[5] = (byte) webMsgDeployRemainChargeTimes.getTimes();
        return bytes;
    }

    /**
     * 构建设备 ID 的CAN帧
     *
     * @param webMsgDeployEmployeeDeviceId 设备ID bean
     * @return 设备ID的 CAN 帧
     */
    public byte[] buildDeviceId(WebMsgDeployEmployeeDeviceId webMsgDeployEmployeeDeviceId) {
        byte[] bytes = buildMsgOutline(webMsgDeployEmployeeDeviceId);
        bytes[0] += 1;
        bytes[5] = (byte) webMsgDeployEmployeeDeviceId.getUpdatedDeviceId();
        return bytes;
    }

    /**
     * 构建员工姓名的 CAN 帧
     *
     * @param webMsgDeployEmployeeName 员工姓名 bean
     * @return 员工姓名的 CAN 帧
     */
    public byte[] buildEmployeeName(WebMsgDeployEmployeeName webMsgDeployEmployeeName) {
        byte[] bytes = buildMsgOutline(webMsgDeployEmployeeName);

        byte[] bytesBody = webMsgDeployEmployeeName.getValue().getBytes(charset_GB2312);
        bytes[0] += bytesBody.length > 8 ? 8 : bytesBody.length;
        addMessageBody(bytes, bytesBody, 0);
        return bytes;
    }

    /**
     * 构建员工单位的 CAN 帧
     *
     * @param webMsgDeployEmployeeDepartment 员工单位 bean
     * @return 员工单位的 CAN 帧
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

        byte[] bytes3 = new byte[13 * 2];
        System.arraycopy(bytes, 0, bytes3, 0, 13);
        System.arraycopy(bytes2, 0, bytes3, 13, 13);
        return bytes3;
    }

    /**
     * 构建员工卡号的 CAN 帧
     *
     * @param webMsgDeployEmployeeCardNumber 员工卡号 bean
     * @return 员工卡号的 CAN 帧
     */
    public byte[] buildEmployeeCardNumber(WebMsgDeployEmployeeCardNumber webMsgDeployEmployeeCardNumber) {
        byte[] bytes = buildMsgOutline(webMsgDeployEmployeeCardNumber);
        bytes[0] += 8;
        byte[] byteCardNum = longToByteArray(webMsgDeployEmployeeCardNumber.getCardNumber());
        addMessageBody(bytes, byteCardNum, 0);
        return bytes;
    }


    /**
     * 构建开锁用的 CAN 帧
     *
     * @param webMsgOperateBoxUnlock 开锁 bean
     * @return 开锁用的 CAN 帧
     */
    public byte[] buildWebUnlock(WebMsgOperateBoxUnlock webMsgOperateBoxUnlock) {
        return buildMsgOutline(webMsgOperateBoxUnlock);
    }

    /**
     * 构建万能卡号 CAN 帧
     *
     * @param webMsgDeployFreeCardNumber 万能卡号 bean
     * @return 万能卡号的 CAN 帧
     */
    public byte[] buildFreeCardNumber(WebMsgDeployFreeCardNumber webMsgDeployFreeCardNumber) {
        long[] cards = webMsgDeployFreeCardNumber.getCardNumbers();
        int count = cards.length < 16 ? cards.length : 16;
        byte[] bytesSend = new byte[13 * count];

        for (int i = 0; i < count; i++) {
            byte[] bytes = buildMsgOutline(webMsgDeployFreeCardNumber);
            bytes[2] += i;
            addMessageBody(bytes, longToByteArray(cards[i]), 0);
            System.arraycopy(bytes, 0, bytesSend, 13 * i, 13);
        }
        return bytesSend;
    }

    /**
     * 「初始化」服务器匹配确认卡号成功
     *
     * @param marchConfirmCardSuccessful 匹配确认卡号成功 bean
     * @return 「匹配确认卡号成功」的 CAN 帧
     */
    public byte[] buildInitMarchConfirmCardSuccessful(WebMsgInitMarchConfirmCardResponse marchConfirmCardSuccessful) {
        byte[] bytes = buildMsgOutline(marchConfirmCardSuccessful);
        bytes[0] += 1;
        bytes[5] = (byte) (marchConfirmCardSuccessful.isSuccessful() ? 1 : 0);
        return bytes;
    }


    /**
     * 构建清除设备初始化状态的 CAN 帧
     *
     * @param clearDeviceStatus 清除设备初始化状态 bean
     * @return 清除设备初始化状态的 CAN 帧
     */
    public byte[] buildClearDeviceStatus(WebMsgInitClearDeviceStatus clearDeviceStatus) {
        return buildMsgOutline(clearDeviceStatus);
    }

    /**
     * 构建 CAN 帧的轮廓，只需再填入数据位即可
     *
     * @param message 欲构建为 CAN 帧的 bean
     * @return 轮廓 CAN 帧
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
