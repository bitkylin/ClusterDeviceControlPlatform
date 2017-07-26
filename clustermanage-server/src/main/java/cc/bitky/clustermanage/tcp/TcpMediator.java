package cc.bitky.clustermanage.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import cc.bitky.clustermanage.server.bean.ServerTcpMessageHandler;
import cc.bitky.clustermanage.server.message.base.BaseTcpResponseMsg;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.send.SendableMsg;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseStatus;
import cc.bitky.clustermanage.server.schedule.SendingMsgRepo;
import cc.bitky.clustermanage.tcp.server.MsgToCanParser;
import io.netty.channel.ChannelPipeline;

/**
 * Netty 模块和上层服务器的中介者
 */
@Service
public class TcpMediator {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private ServerTcpMessageHandler serverTcpMessageHandler;
    private MsgToCanParser msgToCanParser;

    public void setMsgToCanParser(MsgToCanParser msgToCanParser) {
        this.msgToCanParser = msgToCanParser;
    }


    public SendingMsgRepo getSendingMsgRepo() {
        return serverTcpMessageHandler.getSendingMsgRepo();
    }

    public void writeCanToTcp(SendableMsg sendableMsg) {
        List<ChannelPipeline> channelPipelines = getSendingMsgRepo().getChannelPipelines();
        if (channelPipelines.size() == 0) return;
        channelPipelines.forEach(pipe -> pipe.writeAndFlush(sendableMsg));
    }

    /**
     * 直接将 Message 发送至 Netty 的处理通道
     *
     * @param message 普通消息 Message
     * @return 是否发送成功
     */
    public boolean sendMsgToNetty(IMessage message) {
        if (getSendingMsgRepo().getChannelPipelines().size() > 0) {
            msgToCanParser.write(message);
            return true;
        }
        logger.warn("没有可用的 TCP 连接，故该信息无法发送");
        return false;
    }

    public void setServerTcpMessageHandler(ServerTcpMessageHandler serverTcpMessageHandler) {
        this.serverTcpMessageHandler = serverTcpMessageHandler;
    }

    public void handleTcpResponseMsg(BaseTcpResponseMsg msg) {
        serverTcpMessageHandler.handleTcpResponseMsg(msg);
    }

    public void handleResDeviceStatus(TcpMsgResponseStatus msg) {
        serverTcpMessageHandler.handleResDeviceStatus(msg);
    }

    public void handleTcpInitMsg(IMessage msg) {
        serverTcpMessageHandler.handleTcpInitMsg(msg);
    }

    public void handleTcpMsg() {
        serverTcpMessageHandler.handleTcpMsg();
    }
}
