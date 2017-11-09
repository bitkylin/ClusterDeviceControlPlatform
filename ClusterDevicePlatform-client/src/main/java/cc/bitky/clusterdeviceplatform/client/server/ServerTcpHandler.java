package cc.bitky.clusterdeviceplatform.client.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.client.netty.TcpPresenter;
import cc.bitky.clusterdeviceplatform.client.netty.repo.TcpRepository;
import cc.bitky.clusterdeviceplatform.messageutils.define.BaseMsg;

@Service
public class ServerTcpHandler {

    private static final Logger logger = LoggerFactory.getLogger(TcpRepository.class);
    private final TcpPresenter tcpPresenter;

    @Autowired
    public ServerTcpHandler(TcpPresenter tcpPresenter) {
        this.tcpPresenter = tcpPresenter;
        tcpPresenter.setServer(this);
    }


    /**
     * 将指定的消息对象发送至 TCP 通道
     *
     * @param message 指定的消息对象
     */
    public void sendMessage(BaseMsg message) {
        tcpPresenter.sendMessageToTcp(message);
    }

    /**
     * Netty 模块捕获到 Java 消息对象
     *
     * @param message 消息对象
     */
    public void huntMessage(BaseMsg message) {
  //      logger.info("捕获到「普通」消息对象：「" + message.getMsgDetail() + "」");
    }
}
