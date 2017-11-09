package cc.bitky.clusterdeviceplatform.server.tcp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

import cc.bitky.clusterdeviceplatform.server.tcp.handler.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Service
public class NettyServer implements CommandLineRunner {

    private final ServerChannelInitializer serverChannelInitializer;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public NettyServer(ServerChannelInitializer serverChannelInitializer) {
        this.serverChannelInitializer = serverChannelInitializer;
    }

    @Override
    public void run(String... args) throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(serverChannelInitializer);
        ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(30232));
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                logger.info("「Netty」服务器启动成功");
            } else {
                logger.info("「Netty」服务器启动失败");
            }
        });
    }
}
