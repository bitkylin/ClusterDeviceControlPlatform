package cc.bitky.clustermanage.tcp.server.netty;

import cc.bitky.clustermanage.tcp.server.netty.NettyServerContract.IServerPresenter;
import cc.bitky.clustermanage.tcp.server.netty.NettyServerContract.IServerView;

public class NettyServerPresenter implements IServerPresenter {
  private IServerView view;

  NettyServerPresenter(IServerView view) {
    this.view = view;
    view.setPresenter(this);
    start();
  }

  @Override public void start() {

  }
}
