package cc.bitky.clusterdeviceplatform.server.tcp;

import cc.bitky.clusterdeviceplatform.server.config.ServerSetting;
import cc.bitky.clusterdeviceplatform.server.tcp.handler.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

@Slf4j
@Service
public class NettyServer implements CommandLineRunner {

    private final ServerChannelInitializer serverChannelInitializer;

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
        ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(ServerSetting.SERVER_TCP_PORT));
        channelFuture.addListener(future -> {
            if (future.isSuccess()) {
                log.info("「Netty」模块启动成功");
            } else {
                log.info("「Netty」模块启动失败");
            }
        });
    }
}
