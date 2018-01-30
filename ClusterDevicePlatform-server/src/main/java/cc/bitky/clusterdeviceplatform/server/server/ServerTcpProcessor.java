package cc.bitky.clusterdeviceplatform.server.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;
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
        logger.info("发送消息：「" + message.getMsgDetail() + "」");
        return tcpPresenter.sendMessageToTcp(message);
    }

    /**
     * Netty 模块捕获到 Java 消息对象
     *
     * @param message 消息对象
     */
    public void huntMessage(BaseMsg message) {
        logger.info("捕获到「普通」消息对象：「" + message.getMsgDetail() + "」");
    }

    /**
     * Netty 模块捕获到「设备状态」消息对象
     *
     * @param message 消息对象
     */
    public void huntDeviceStatusMsg(MsgReplyDeviceStatus message) {
        logger.info("捕获到「待处理」消息对象：「" + message.getMsgDetail() + "」");
        centerProcessor.getTcpFeedBackRepository().recoveryItem(message.getGroupId());
        centerProcessor.huntDeviceStatusMsg(message);
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
