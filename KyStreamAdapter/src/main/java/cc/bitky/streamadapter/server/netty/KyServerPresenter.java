package cc.bitky.streamadapter.server.netty;

import cc.bitky.streamadapter.server.netty.ServerContract.IServerPresenter;
import cc.bitky.streamadapter.server.netty.ServerContract.IServerView;

public class KyServerPresenter implements IServerPresenter {
  private IServerView view;

  KyServerPresenter(IServerView view) {
    this.view = view;
    view.setPresenter(this);
    start();
  }

  @Override public void start() {

  }
}
