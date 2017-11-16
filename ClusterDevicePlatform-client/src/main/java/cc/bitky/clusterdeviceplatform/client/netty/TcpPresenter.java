package cc.bitky.clusterdeviceplatform.client.netty;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingDeque;

import cc.bitky.clusterdeviceplatform.client.netty.repo.TcpRepository;
import cc.bitky.clusterdeviceplatform.client.server.ServerTcpHandler;
import cc.bitky.clusterdeviceplatform.messageutils.MsgProcessor;
import cc.bitky.clusterdeviceplatform.messageutils.config.JointMsgType;
import cc.bitky.clusterdeviceplatform.messageutils.define.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.FrameMajorHeader;
import cc.bitky.clusterdeviceplatform.messageutils.msg.MsgReplyNormal;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecHeartbeat;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.MsgCodecReplyNormal;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * TCP 通道的中介者
 */
@Service
public class TcpPresenter {

    private final MsgProcessor msgProcessor;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TcpRepository tcpRepository;
    private ServerTcpHandler server;

    @Autowired
    public TcpPresenter(TcpRepository tcpRepository) {
        this.tcpRepository = tcpRepository;
        this.msgProcessor = MsgProcessor.getInstance();
    }

    public void setServer(ServerTcpHandler server) {
        this.server = server;
    }

    /**
     * 「内部使用」将 TCP 帧解码并转换为消息对象，而后传递至 Server
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
        logger.info("收到正常消息对象：「" + msg.getMsgDetail() + "」");
        switch (msg.getJointMsgFlag()) {
            case JointMsgType.ChargeStatus:
                break;
            case JointMsgType.HeartBeat:
                Attribute<Integer> key = channel.attr(AttributeKey.valueOf("ID"));
                MsgReplyNormal replyHeartbeat = MsgCodecReplyNormal.createByBaseMsg(MsgCodecHeartbeat.create(key.get()));
                logger.info("已生成并发送正常回复消息对象：「" + replyHeartbeat.getMsgDetail() + "」");
                sendMessageToTcp(replyHeartbeat);
                break;
            default:
                //剩下的均为正常需回复消息
                server.huntMessage(msg);
                MsgReplyNormal replyNormal = MsgCodecReplyNormal.createByBaseMsg(msg);
                if (replyNormal == null) {
                    logger.warn("生成正常回复消息对象出错");
                    return;
                }
                logger.info("已生成并发送正常回复消息对象：「" + replyNormal.getMsgDetail() + "」");
                sendMessageToTcp(replyNormal);
        }
    }

    /**
     * 将消息对象下发至 TCP 通道
     *
     * @param message 消息对象
     * @return 是否发送成功
     */
    public boolean sendMessageToTcp(BaseMsg message) {
        Channel channel = TcpRepository.touchChannel(message.getGroupId());
        LinkedBlockingDeque<BaseMsg> deque = tcpRepository.touchMessageQueue(message.getGroupId());
        if (channel == null || deque == null) {
            return false;
        }
        deque.offer(message);
        return true;
    }

    /**
     * 特定的 Channel 已激活
     *
     * @param channel 已激活的 Channel
     */
    public void channelActive(Channel channel) {
        tcpRepository.activeChannel(channel);
    }

    /**
     * 特定的 Channel 已断开连接
     *
     * @param i Channel 的 ID
     */
    public void channelInactive(int i) {
        tcpRepository.inactiveChannel(i);
    }
}
