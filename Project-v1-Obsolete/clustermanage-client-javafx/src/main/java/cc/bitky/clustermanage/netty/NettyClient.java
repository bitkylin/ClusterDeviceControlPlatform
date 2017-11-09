package cc.bitky.clustermanage.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import cc.bitky.clustermanage.netty.handler.KyChannelInitializer;
import cc.bitky.clustermanage.utils.SuccessfulListener;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

public class NettyClient {

    private SuccessfulListener launchListener;
    private SuccessfulListener finishListener;
    private EventLoopGroup group;


    public void start(String hostName, int port) {
        Executors.newSingleThreadExecutor().submit(() -> {
            group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new KyChannelInitializer());
            if (hostName != null && !hostName.equals(""))
                bootstrap.remoteAddress(new InetSocketAddress(hostName, port));
            else
                bootstrap.remoteAddress(new InetSocketAddress(port));
            ChannelFuture channelFuture = null;

            try {
                channelFuture = bootstrap.connect().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            startListenerHandle(channelFuture, launchListener);
        });
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
