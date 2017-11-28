package cc.bitky.clusterdeviceplatform.client.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.client.netty.TcpPresenter;
import cc.bitky.clusterdeviceplatform.client.ui.view.MainView;
import cc.bitky.clusterdeviceplatform.messageutils.define.BaseMsg;

@Service
public class ServerTcpHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ApplicationContext appContext;
    private TcpPresenter tcpPresenter;
    private MainView ui;

    @Autowired
    public ServerTcpHandler(ApplicationContext appContext,TcpPresenter tcpPresenter) {
        this.appContext = appContext;
        this.tcpPresenter = tcpPresenter;
        tcpPresenter.setServer(this);
    }


    /**
     * 启动特定编号的客户端
     *
     * @param groupId 欲启动的客户端编号
     */
    public void startClient(int groupId) {
        tcpPresenter.startClient(groupId);
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

    }

    /**
     * Netty 服务器优雅关闭
     */
    public void shutdown() {
        System.exit(SpringApplication.exit(appContext));
    }

    public void setUi(MainView ui) {
        this.ui = ui;
    }
}
