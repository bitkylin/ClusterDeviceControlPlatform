package cc.bitky.clustermanage.tcp.server.netty;

import cc.bitky.clustermanage.tcp.base.BasePresenter;
import cc.bitky.clustermanage.tcp.base.BaseView;
import cc.bitky.clustermanage.tcp.util.listener.SuccessfulListener;

public class NettyServerContract {

  public interface IServerView extends BaseView<IServerPresenter> {
    void setLaunchSuccessfulListener(SuccessfulListener successfulListener);
    void setFinishSuccessfulListener(SuccessfulListener successfulListener);

    void start();

    void shutdown();
  }

  interface IServerPresenter extends BasePresenter {

  }
}
