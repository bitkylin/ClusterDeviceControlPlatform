package cc.bitky.clustermanage.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.IMessage;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseEmployeeCardnumber;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseEmployeeDepartment;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseEmployeeDepartment2;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseEmployeeName;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseFreeCardNumber;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseOperateBoxUnlock;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseRemainChargeTimes;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeDepartment2;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployFreeCardSpecial;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployRemainChargeTimes;
import cc.bitky.clustermanage.netty.message.web.WebMsgInitMarchConfirmCardResponse;
import cc.bitky.clustermanage.view.Container;
import cc.bitky.clustermanage.view.MainView;
import cc.bitky.clustermanage.view.bean.Device;
import cc.bitky.clustermanage.view.bean.DeviceKey;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ParsedMessageInBoundHandler extends SimpleChannelInboundHandler<IMessage> {

    private int sum = 0;
    private int errorSum = 0;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMessage msg) {
        logger.debug("------收到CAN帧「msgId=" + msg.getMsgId() + ", groupId=" + msg.getGroupId() + ", boxId=" + msg.getBoxId() + "」------");
        MainView.getInstance().updateGroupCount(msg.getGroupId());
        logger.warn("#%#%收到帧的数量：" + ++sum);
        MainView.getInstance().remoteUpdateDevice(sum);
        if (errorSum != 0)
            logger.warn("#%#%最终未能解析帧的数量：" + errorSum);
        Device device;

        switch (msg.getMsgId()) {
            case MsgType.SERVER_REQUSET_STATUS:
                logger.debug("收到操作：状态请求");
                break;

            case MsgType.SERVER_SET_REMAIN_CHARGE_TIMES:
                WebMsgDeployRemainChargeTimes remainChargeTimes = (WebMsgDeployRemainChargeTimes) msg;
                logger.debug("收到剩余充电次数更新: " + remainChargeTimes.getTimes());
                device = getDevice(msg);
                addHistory(device, "充电次数:" + remainChargeTimes.getTimes());
                MainView.getInstance().remoteUpdateDevice(sum, device);
                ctx.channel().writeAndFlush(new TcpMsgResponseRemainChargeTimes(device.getGroupId(), device.getDeviceId(), device.getStatus() > 4 ? 0 : 1));
                break;

            case MsgType.SERVER_SET_DEVICE_ID:
                WebMsgDeployEmployeeDeviceId deployEmployeeDeviceId = (WebMsgDeployEmployeeDeviceId) msg;
                logger.debug("收到部署设备 Id 更新: " + deployEmployeeDeviceId.getUpdatedDeviceId());
                DeviceKey deviceKeyOld = new DeviceKey(msg.getGroupId(), msg.getBoxId());
                DeviceKey deviceKeyNew = new DeviceKey(msg.getGroupId(), deployEmployeeDeviceId.getUpdatedDeviceId());
                Device deviceOld = Container.deviceHashMap.remove(deviceKeyNew);
                device = Container.deviceHashMap.remove(deviceKeyOld);
                if (device == null) {
                    device = new Device(msg.getGroupId(), deployEmployeeDeviceId.getUpdatedDeviceId());
                }
                device.setDeviceId(deployEmployeeDeviceId.getUpdatedDeviceId());
                addHistory(device, "Id:" + deployEmployeeDeviceId.getUpdatedDeviceId());
                Container.deviceHashMap.put(deviceKeyNew, device);
                MainView.getInstance().remoteUpdateDevice(sum, deviceOld, device);
                break;

            case MsgType.SERVER_SET_EMPLOYEE_NAME:
                WebMsgDeployEmployeeName webMsgDeployEmployeeName = (WebMsgDeployEmployeeName) msg;
                logger.debug("收到部署员工姓名更新: " + webMsgDeployEmployeeName.getValue());
                device = getDevice(msg);
                device.setName(webMsgDeployEmployeeName.getValue());
                addHistory(device, "姓名:" + webMsgDeployEmployeeName.getValue());
                MainView.getInstance().remoteUpdateDevice(sum, device);
                ctx.channel().writeAndFlush(new TcpMsgResponseEmployeeName(device.getGroupId(), device.getDeviceId(), device.getStatus() > 4 ? 0 : 1));
                break;

            case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_1:
                WebMsgDeployEmployeeDepartment deployEmployeeDepartment1 = (WebMsgDeployEmployeeDepartment) msg;
                logger.debug("收到部署员工单位「1」更新: " + deployEmployeeDepartment1.getValue());
                device = getDevice(msg);
                device.setDepartment(deployEmployeeDepartment1.getValue());
                addHistory(device, "单位1:" + device.getDepartment());
                MainView.getInstance().remoteUpdateDevice(sum, device);
                ctx.channel().writeAndFlush(new TcpMsgResponseEmployeeDepartment(device.getGroupId(), device.getDeviceId(), device.getStatus() > 4 ? 0 : 1));
                break;

            case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_2:
                WebMsgDeployEmployeeDepartment2 deployEmployeeDepartment2 = (WebMsgDeployEmployeeDepartment2) msg;
                logger.debug("收到部署员工单位「2」更新: " + deployEmployeeDepartment2.getValue());
                device = getDevice(msg);
                device.setDepartment(device.getDepartment() + deployEmployeeDepartment2.getValue());
                addHistory(device, "单位2:" + deployEmployeeDepartment2.getValue());
                MainView.getInstance().remoteUpdateDevice(sum, device);
                ctx.channel().writeAndFlush(new TcpMsgResponseEmployeeDepartment2(device.getGroupId(), device.getDeviceId(), device.getStatus() > 4 ? 0 : 1));
                break;

            case MsgType.SERVER_SET_EMPLOYEE_CARD_NUMBER:
                WebMsgDeployEmployeeCardNumber deployEmployeeCardNumber = (WebMsgDeployEmployeeCardNumber) msg;
                logger.debug("收到部署员工卡号更新: " + deployEmployeeCardNumber.getCardNumber());
                device = getDevice(msg);
                device.setCardNumber(deployEmployeeCardNumber.getCardNumber());
                addHistory(device, "卡号:" + deployEmployeeCardNumber.getCardNumber());
                MainView.getInstance().remoteUpdateDevice(sum, device);
                ctx.channel().writeAndFlush(new TcpMsgResponseEmployeeCardnumber(device.getGroupId(), device.getDeviceId(), device.getStatus() > 4 ? 0 : 1));
                break;

            case MsgType.SERVER_REMOTE_UNLOCK:
                logger.debug("收到操作：远程开锁");
                device = getDevice(msg);
                addHistory(device, "开锁");
                MainView.getInstance().remoteUpdateDevice(sum, device);
                ctx.channel().writeAndFlush(new TcpMsgResponseOperateBoxUnlock(device.getGroupId(), device.getDeviceId(), device.getStatus() > 4 ? 0 : 1));
                break;

            case MsgType.SERVER_SET_FREE_CARD_NUMBER:
                WebMsgDeployFreeCardSpecial deployFreeCardSpecial = (WebMsgDeployFreeCardSpecial) msg;
                logger.debug("收到部署万能卡号「『" + deployFreeCardSpecial.getItemId() + "』" + deployFreeCardSpecial.getCardNumber() + "」");
                device = getDevice(msg);
                addHistory(device, "万能:" + deployFreeCardSpecial.getItemId());
                MainView.getInstance().remoteUpdateDevice(sum, device);
                ctx.channel().writeAndFlush(new TcpMsgResponseFreeCardNumber(device.getGroupId(), device.getDeviceId(), device.getStatus() > 4 ? 0 : 1, deployFreeCardSpecial.getItemId()));
                break;

            case MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_RESPONSE:
                WebMsgInitMarchConfirmCardResponse marchConfirmCardResponse = (WebMsgInitMarchConfirmCardResponse) msg;
                logger.debug("「初始化」 匹配确认卡号状态: " + marchConfirmCardResponse.isSuccessful());
                device = getDevice(msg);
                addHistory(device, "匹配:" + marchConfirmCardResponse.isSuccessful());
                MainView.getInstance().remoteUpdateDevice(sum, device);
                break;

            case MsgType.INITIALIZE_SERVER_CLEAR_INITIALIZE_MESSAGE:
                logger.debug("「初始化」 清除设备的初始化状态");
                device = getDevice(msg);
                device.setName("");
                device.setDepartment("");
                device.setCardNumber("0");
                device.setStatus(0);
                addHistory(device, "清空设备");
                MainView.getInstance().remoteUpdateDevice(sum, device);
                break;

            default:
                logger.warn("无法解析正确的 Message");
                errorSum++;
                break;
        }
    }

    /**
     * 在从 HashMap 中读取的设备 bean 中添加历史记录
     *
     * @param device   从 HashMap 中读取的设备 bean
     * @param addValue 欲添加的信息
     */
    private void addHistory(Device device, String addValue) {
        String finalStr = (device.getHistoryList().size() + 1) + ". " + addValue;
        device.addHistoryList(finalStr);
    }

    /**
     * 获得存储在 HashMap 中的单个设备 bean
     *
     * @param msg 收到的 Message
     * @return 读取到的设备 bean
     */
    private Device getDevice(IMessage msg) {
        DeviceKey deviceKey = new DeviceKey(msg.getGroupId(), msg.getBoxId());
        Device device = Container.deviceHashMap.get(deviceKey);
        if (device == null) {
            device = new Device(msg.getGroupId(), msg.getBoxId());
            Container.deviceHashMap.put(deviceKey, device);
        }
        return device;
    }

    /**
     * 清除接收帧的计数
     *
     * @param rec 是否清除全部帧的计数
     * @param err 是否清除错误帧的计数
     */
    void clearRecCount(boolean rec, boolean err) {
        if (rec) sum = 0;
        if (err) errorSum = 0;
    }
}

