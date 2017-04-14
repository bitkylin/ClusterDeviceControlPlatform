package cc.bitky.clustermanage.server.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.tcp.server.netty.channelhandler.ServerChannelInitializer;

@Service
public class ServerWebMessageHandler {
    private ServerChannelInitializer serverChannelInitializer;
    private Logger logger = LoggerFactory.getLogger(ServerWebMessageHandler.class);

    @Autowired
    public ServerWebMessageHandler(ServerChannelInitializer serverChannelInitializer) {
        this.serverChannelInitializer = serverChannelInitializer;
    }


    /**
     * 服务器处理「 Web 信息 bean 」并写入 Netty 的 Handler
     *
     * @param message Web信息 bean 的集合
     * @return 是否成功写入 Netty 处理通道
     */
    public boolean handleWebMsg(List<IMessage> message) {
        if (serverChannelInitializer.getPipeline() == null) return false;
        serverChannelInitializer.getPipeline().write(message);
        return true;
    }
}
