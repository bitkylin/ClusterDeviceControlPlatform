package cc.bitky.clusterdeviceplatform.client.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import cc.bitky.clusterdeviceplatform.client.config.CommSetting;
import cc.bitky.clusterdeviceplatform.client.netty.handler.ClientChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

@Service
public class NettyClient {

    private final ClientChannelInitializer clientChannelInitializer;
    private final EventLoopGroup group = new NioEventLoopGroup();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final AttributeKey<Integer> channelId = AttributeKey.newInstance("ID");

    @Autowired
    public NettyClient(TcpPresenter tcpPresenter, ClientChannelInitializer clientChannelInitializer) {
        tcpPresenter.setNettyClient(this);
        this.clientChannelInitializer = clientChannelInitializer;
    }

    public void start(int id) {
        Executors.newSingleThreadExecutor().submit(() ->
                startClient(CommSetting.SERVER_HOSTNAME, CommSetting.SERVER_PORT, id));
    }

    private void startClient(String hostName, int port, int id)  {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .attr(channelId, id)
                .channel(NioSocketChannel.class)
                .handler(clientChannelInitializer);
        if (hostName != null && !"".equals(hostName)) {
            try {
                bootstrap.remoteAddress(new InetSocketAddress(InetAddress.getByName(hostName), port));
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return;
            }
            //     bootstrap.remoteAddress(new InetSocketAddress(hostName, port));
        } else {
            bootstrap.remoteAddress(new InetSocketAddress(port));
        }
        try {
            bootstrap.connect().sync().addListener(future -> logger.info("Channel「" + id + "」" + "已连接"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
