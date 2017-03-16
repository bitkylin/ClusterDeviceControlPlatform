package cc.bitky.streamadapter.server.netty;

import cc.bitky.streamadapter.server.netty.ServerContract.IServerPresenter;
import cc.bitky.streamadapter.server.netty.ServerContract.IServerView;
import cc.bitky.streamadapter.server.netty.channelhandler.ServerChannelInitializer;
import cc.bitky.streamadapter.util.listener.FinishSuccessfulListener;
import cc.bitky.streamadapter.util.listener.LaunchSuccessfulListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import java.net.InetSocketAddress;

public class KyServer implements IServerView {
  private LaunchSuccessfulListener launchListener;
  private FinishSuccessfulListener finishListener;
  private NioEventLoopGroup group;

  public KyServer() {

  }

  @Override public void setPresenter(IServerPresenter presenter) {

  }

  @Override public void start() {
    new Thread(() -> {
      group = new NioEventLoopGroup();
      ServerBootstrap bootstrap = new ServerBootstrap();
      bootstrap.group(group)
          .channel(NioServerSocketChannel.class)
          .childHandler(new ServerChannelInitializer());
      ChannelFuture channelFuture = bootstrap.bind(new InetSocketAddress(30232));
      channelFuture.addListener((ChannelFutureListener) future -> {

        if (future.isSuccess()) {
          if (launchListener != null) launchListener.onSuccess(true);
        } else {
          future.cause().printStackTrace();
          if (launchListener != null) launchListener.onSuccess(false);
        }
      });
    }).start();
  }

  public void setLaunchSuccessfulListener(
      LaunchSuccessfulListener successfulListener) {
    this.launchListener = successfulListener;
  }

  public void setFinishSuccessfulListener(
      FinishSuccessfulListener finishListener) {
    this.finishListener = finishListener;
  }

  @Override public void shutdown() {
    if (group != null) {

      Future<?> futureShutdown = group.shutdownGracefully();
      futureShutdown.addListener((future) -> {
        if (future.isSuccess()) {
          System.out.println("服务器优雅关闭成功");
          if (finishListener != null) finishListener.onSuccess(true);
        } else {
          System.out.println("服务器优雅关闭失败");
          future.cause().printStackTrace();
          if (finishListener != null) finishListener.onSuccess(false);
        }
      });
    }
  }
}
