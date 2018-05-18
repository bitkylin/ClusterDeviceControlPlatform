package cc.bitky.clusterdeviceplatform.server.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import cc.bitky.clusterdeviceplatform.messageutils.MsgProcessor;
import cc.bitky.clusterdeviceplatform.messageutils.config.JointMsgType;
import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.FrameMajorHeader;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.SendableMsgContainer;
import cc.bitky.clusterdeviceplatform.messageutils.dynamicsetting.DefaultEmployeeInfo;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyNormal;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.controlcenter.MsgCodecHeartbeat;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.controlcenter.MsgCodecTimestamp;
import cc.bitky.clusterdeviceplatform.server.config.DbSetting;
import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.server.ServerTcpProcessor;
import cc.bitky.clusterdeviceplatform.server.tcp.repo.TcpRepository;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.channel.ChannelOutline;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.except.TcpFeedbackItem;
import io.netty.channel.Channel;

/**
 * TCP 通道的中介者
 */
@Service
public class TcpPresenter {
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(DeviceSetting.MAX_GROUP_ID);
    private static final Logger logger = LoggerFactory.getLogger(TcpPresenter.class);
    private final TcpRepository tcpRepository;
    private final MsgProcessor msgProcessor;
    private ServerTcpProcessor server;

    @Autowired
    public TcpPresenter(TcpRepository tcpRepository) {
        this.tcpRepository = tcpRepository;
        tcpRepository.setServer(this);
        this.msgProcessor = MsgProcessor.getInstance();
        MsgProcessor.setDeviceDefaultDeploy(new DefaultEmployeeInfo(DbSetting.DEFAULT_EMPLOYEE_CARD_NUMBER, DbSetting.DEFAULT_EMPLOYEE_NAME, DbSetting.DEFAULT_EMPLOYEE_DEPARTMENT));
    }

    /**
     * 将消息对象下发至 TCP 通道
     *
     * @param message 消息对象
     * @return 是否发送成功
     */
    public boolean sendMessageToTcp(BaseMsg message) {
        if (channelIsActivated(message.getGroupId())) {
            LinkedBlockingDeque<BaseMsg> deque = tcpRepository.touchMessageQueue(message.getGroupId());
            if (message.isPrior()) {
                deque.offerFirst(message);
            } else {
                deque.offer(message);
            }
            return true;
        }
        return false;
    }

    /**
     * 根据设备组 ID，判断指定的 Channel 是否激活
     *
     * @param groupId 特定的设备组 ID
     * @return 指定的 Channel 是否激活
     */
    public boolean channelIsActivated(int groupId) {
        Channel channel = tcpRepository.touchChannel(groupId);
        return channel != null && channel.isActive();
    }

    public void setServer(ServerTcpProcessor server) {
        this.server = server;
    }

    /**
     * Channel 激活
     *
     * @param channel 待激活的 Channel
     */
    public void channelActive(Channel channel) {
        //启动计时器，用于心跳包检测，超时后即关闭该 Channel
        tcpRepository.accessibleChannel(channel);
        //发送心跳包，从而确定 Channel 的ID
        SendableMsgContainer msgContainer = msgProcessor.buildSendableMsgDirectly(MsgCodecHeartbeat.create(), channel);
        channel.writeAndFlush(msgContainer);
    }

    /**
     * 「内部使用」将 TCP 帧解码并转换为消息对象，而后传递至 Server 该方法可保证解析出的消息对象的设备号正确
     *
     * @param head     TCP 帧头
     * @param subMsgId TCP 子帧功能位
     * @param data     TCP 子帧数据体
     * @param channel  该消息帧的容器 Channel
     */
    public void decodeAndHuntMessage(FrameMajorHeader head, int subMsgId, byte[] data, Channel channel) {
        BaseMsg msg = msgProcessor.decode(head, subMsgId, data);
        if (msg == null) {
            logger.warn("帧解析出错");
            return;
        }
        if (msg.getGroupId() > DeviceSetting.MAX_GROUP_ID && msg.getDeviceId() > DeviceSetting.MAX_DEVICE_ID) {
            logger.warn("设备号出错「GroupId:" + msg.getGroupId() + "; DeviceId:" + msg.getDeviceId() + "」");
            return;
        }
        switch (msg.getJointMsgFlag()) {
            case JointMsgType.replyWorkStatus:
            case JointMsgType.replyChargeStatus:
                server.huntDeviceStatusMsg((MsgReplyDeviceStatus) msg);
                break;
            case JointMsgType.replyHeartBeat:
                // 收到心跳包，设备组已激活
                int groupId = msg.getGroupId();
                //设定设备时间校对计划任务
                ScheduledFuture future = channel.eventLoop().scheduleAtFixedRate(
                        () -> sendMessageToTcp(MsgCodecTimestamp.create(groupId)), 1, DeviceSetting.TIMESYNC_INTERVAL, TimeUnit.SECONDS);
                tcpRepository.accessChannelSuccessful(msg, channel, future);
                break;
            default:
                if (msg instanceof MsgReplyNormal) {
                    tcpRepository.touchNormalReplyMsg((MsgReplyNormal) msg);
                    server.touchNormalReplyMsg((MsgReplyNormal) msg);
                } else {
                    server.huntMessage(msg);
                }
        }
    }

    /**
     * Channel 已断开，进行扫尾工作并向上级反馈
     *
     * @param channel 已断开的 Channel
     */
    public void channelInactiveCompleted(Channel channel) {
        int channelIndex = tcpRepository.removeChannelCompleted(channel);
        server.touchUnusualMsg(TcpFeedbackItem.createChannelDisconnect(channelIndex));
    }

    /**
     * 「内部接口」捕获到异常消息对象时，回调该接口传出异常信息并向上级反馈
     *
     * @param msg 一场消息对象
     */
    public void touchUnusualMsg(TcpFeedbackItem msg) {
        logger.info("捕获到异常消息：" + msg.getDescription() + "原始消息：「" + msg.getBaseMsg().msgDetailToString() + "」");
        server.touchUnusualMsg(msg);
    }

    public void shutDown() {
        tcpRepository.removeAllChannel();
    }

    //--------------- 数据统计 ---------------

    /**
     * 统计所有 Channel 的当前待发送消息数量
     */
    public ChannelOutline statisticChannelLoad() {
        return tcpRepository.statisticChannelLoad();
    }

    /**
     * 「内部接口」通道无响应时的回调方法
     *
     * @param channel 无响应的通道
     */
    public void channelNoResponse(Channel channel) {
        int channelId = tcpRepository.getGroupIdByChannel(channel);
        server.touchUnusualMsg(TcpFeedbackItem.createChannelNoResponse(channelId));
    }
}
