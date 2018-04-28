package cc.bitky.clusterdeviceplatform.server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyNormal;
import cc.bitky.clusterdeviceplatform.server.tcp.TcpPresenter;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.channel.ChannelOutline;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.except.TcpFeedbackItem;

@Service
public class ServerTcpProcessor {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TcpPresenter tcpPresenter;
    private final ServerCenterProcessor centerProcessor;

    @Autowired
    public ServerTcpProcessor(TcpPresenter tcpPresenter, ServerCenterProcessor centerProcessor) {
        this.tcpPresenter = tcpPresenter;
        this.centerProcessor = centerProcessor;
        tcpPresenter.setServer(this);
        centerProcessor.setTcpProcessor(this);
    }

    /**
     * 将指定的消息对象发送至 TCP 通道
     *
     * @param message 指定的消息对象
     */
    boolean sendMessage(BaseMsg message) {
        logger.info("发送消息：「" + message.msgDetailToString() + "」");
        return tcpPresenter.sendMessageToTcp(message);
    }

    /**
     * 服务模块捕获到「正常回复」消息对象
     *
     * @param message 消息对象
     */
    public void touchNormalReplyMsg(MsgReplyNormal message) {
        logger.info("捕获到「正常回复」消息对象：「" + message.msgDetailToString() + "」");

    }
    /**
     * 服务模块捕获到「特殊反馈」消息对象
     *
     * @param message 消息对象
     */
    public void huntMessage(BaseMsg message) {
        logger.info("捕获到「特殊反馈」消息对象：「" + message.msgDetailToString() + "」");
    }

    /**
     * 服务模块捕获到「设备状态」消息对象
     *
     * @param message 消息对象
     */
    public void huntDeviceStatusMsg(MsgReplyDeviceStatus message) {
        logger.info("捕获到「设备状态」消息对象：「" + message.msgDetailToString() + "」");
        centerProcessor.getTcpFeedBackRepository().recoveryItem(message.getGroupId());
        centerProcessor.huntDeviceStatusMsg(message);
    }

    /**
     * 根据待发送的设备组 ID，判断指定的 Channel 是否可用
     *
     * @param groupId 指定的设备组 ID
     * @return 指定的 Channel 是否可用
     */
    public boolean tcpIsAvailable(int groupId) {
        return tcpPresenter.channelIsActivated(groupId);
    }

    /**
     * 「内部接口」发生异常时回调该接口传出异常信息
     *
     * @param msg 一场消息对象
     */
    public void touchUnusualMsg(TcpFeedbackItem msg) {
        centerProcessor.getTcpFeedBackRepository().putItem(msg);
    }

    public void shutDown() {
        tcpPresenter.shutDown();
    }

    //--------------- 数据统计 ---------------

    /**
     * 统计所有 Channel 概览信息
     */
    public ChannelOutline statisticChannelLoad() {
        return tcpPresenter.statisticChannelLoad();
    }


}
