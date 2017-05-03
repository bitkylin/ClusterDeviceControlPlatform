package cc.bitky.clustermanage.netty;

import java.net.InetSocketAddress;

import cc.bitky.clustermanage.netty.handler.KyChannelInitializer;
import cc.bitky.clustermanage.utils.SuccessfulListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

public class NettyServer {

    private SuccessfulListener launchListener;
    private SuccessfulListener finishListener;
    private EventLoopGroup group;


    public void start() {
        new Thread(() -> {
            group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(30232))
                    .handler(KyChannelInitializer.newInstance());
            ChannelFuture channelFuture = null;

            try {
                channelFuture = bootstrap.connect().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startListenerHandle(channelFuture, launchListener);
        }).start();
    }

    private void startListenerHandle(Future future, SuccessfulListener listener) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!future.isSuccess()) future.cause().printStackTrace();
        if (listener != null) listener.onSuccess(future.isSuccess());
    }

    public void setLaunchSuccessfulListener(
            SuccessfulListener successfulListener) {
        this.launchListener = successfulListener;
    }

    public void setFinishSuccessfulListener(
            SuccessfulListener finishListener) {
        this.finishListener = finishListener;
    }

    public void shutdown() {
        if (group != null) {
            Future<?> futureShutdown = group.shutdownGracefully();
            futureShutdown.addListener(future -> startListenerHandle(future, finishListener));
        }
    }
}
