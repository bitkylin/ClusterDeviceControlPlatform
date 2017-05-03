package cc.bitky.clustermanage.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.bitky.clustermanage.netty.WebMsgDeployEmployeeDepartment2;
import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.WebMsgDeployFreeCardSpecial;
import cc.bitky.clustermanage.netty.message.base.IMessage;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeCardNumber;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeDepartment;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeDeviceId;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployEmployeeName;
import cc.bitky.clustermanage.netty.message.web.WebMsgDeployRemainChargeTimes;
import cc.bitky.clustermanage.netty.message.web.WebMsgInitCardException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ParsedMessageInBoundHandler extends SimpleChannelInboundHandler<IMessage> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMessage msg) {
        logger.debug("------收到CAN帧「msgId=" + msg.getMsgId() + ", groupId=" + msg.getGroupId() + ", boxId=" + msg.getBoxId() + "」------");
        switch (msg.getMsgId()) {
            case MsgType.SERVER_REQUSET_STATUS:
                logger.debug("收到操作：状态请求");
                break;
            case MsgType.SERVER_SET_REMAIN_CHARGE_TIMES:
                WebMsgDeployRemainChargeTimes remainChargeTimes = (WebMsgDeployRemainChargeTimes) msg;
                logger.debug("收到剩余充电次数更新: " + remainChargeTimes.getTimes());
                break;
            case MsgType.SERVER_SET_DEVICE_ID:
                WebMsgDeployEmployeeDeviceId deployEmployeeDeviceId = (WebMsgDeployEmployeeDeviceId) msg;
                logger.debug("收到部署设备 Id 更新: " + deployEmployeeDeviceId.getUpdatedDeviceId());
                break;
            case MsgType.SERVER_SET_EMPLOYEE_NAME:
                WebMsgDeployEmployeeName webMsgDeployEmployeeName = (WebMsgDeployEmployeeName) msg;
                logger.debug("收到部署员工姓名更新: " + webMsgDeployEmployeeName.getValue());
                break;
            case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_1:
                WebMsgDeployEmployeeDepartment deployEmployeeDepartment1 = (WebMsgDeployEmployeeDepartment) msg;
                logger.debug("收到部署员工单位「1」更新: " + deployEmployeeDepartment1.getValue());
                break;
            case MsgType.SERVER_SET_EMPLOYEE_DEPARTMENT_2:
                WebMsgDeployEmployeeDepartment2 deployEmployeeDepartment2 = (WebMsgDeployEmployeeDepartment2) msg;
                logger.debug("收到部署员工单位「2」更新: " + deployEmployeeDepartment2.getValue());
                break;
            case MsgType.SERVER_SET_EMPLOYEE_CARD_NUMBER:
                WebMsgDeployEmployeeCardNumber deployEmployeeCardNumber = (WebMsgDeployEmployeeCardNumber) msg;
                logger.debug("收到部署员工卡号更新: " + deployEmployeeCardNumber.getCardNumber());
                break;
            case MsgType.SERVER_REMOTE_UNLOCK:
                logger.debug("收到操作：远程开锁");
                break;
            case MsgType.SERVER_SET_FREE_CARD_NUMBER:
                WebMsgDeployFreeCardSpecial deployFreeCardSpecial = (WebMsgDeployFreeCardSpecial) msg;
                logger.debug("收到部署万能卡号「『" + deployFreeCardSpecial.getItemId() + "』" + deployFreeCardSpecial.getCardNumber() + "」");
                break;
            case MsgType.INITIALIZE_SERVER_DEPLOY_MESSAGE_COMPLETE:
                logger.debug("「初始化」 信息发送完毕");
                break;
            case MsgType.INITIALIZE_SERVER_MARCH_CONFIRM_CARD_SUCCESSFUL:
                logger.debug("「初始化」 匹配确认卡号成功");
                break;
            case MsgType.INITIALIZE_SERVER_MARCH_CARD_EXCEPTION:
                WebMsgInitCardException initCardException = (WebMsgInitCardException) msg;
                logger.debug("「初始化」 匹配卡号失败: " + initCardException.getCardType().toString());
                break;
        }
    }
}

