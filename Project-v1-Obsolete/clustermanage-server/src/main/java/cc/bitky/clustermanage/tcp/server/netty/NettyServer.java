package cc.bitky.clustermanage.tcp.server.netty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;

import cc.bitky.clustermanage.tcp.server.channelhandler.ServerChannelInitializer;
import cc.bitky.clustermanage.tcp.server.netty.NettyServerContract.IServerPresenter;
import cc.bitky.clustermanage.tcp.server.netty.NettyServerContract.IServerView;
import cc.bitky.clustermanage.tcp.util.listener.SuccessfulListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;

@Service
public class NettyServer implements IServerView {
    private final ServerChannelInitializer serverChannelInitializer;
    private SuccessfulListener launchListener;
    private SuccessfulListener finishListener;
    private NioEventLoopGroup group;

    @Autowired
    public NettyServer(ServerChannelInitializer serverChannelInitializer) {
        this.serverChannelInitializer = serverChannelInitializer;
    }

    @Override
    public void setPresenter(IServerPresenter presenter) {

    }

    @Override
    public void start() {
        new Thread(() -> {
            group = new NioEventLoopGroup();
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(serverChannelInitializer);
            ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(30232));
            channelFuture.addListener(future -> startListenerHandle(future, launchListener));
        }).start();
    }

    private void startListenerHandle(Future future, SuccessfulListener listener) {
        if (!future.isSuccess()) {
            future.cause().printStackTrace();
        }
        if (listener != null) {
            listener.onSuccess(future.isSuccess());
        }
    }

    @Override
    public void setLaunchSuccessfulListener(SuccessfulListener successfulListener) {
        this.launchListener = successfulListener;
    }
    @Override
    public void setFinishSuccessfulListener(SuccessfulListener finishListener) {
        this.finishListener = finishListener;
    }

    @Override
    public void shutdown() {
        if (group != null) {
            Future<?> futureShutdown = group.shutdownGracefully();
            futureShutdown.addListener(future -> startListenerHandle(future, finishListener));
        }
    }
}

