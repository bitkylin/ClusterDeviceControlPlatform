package cc.bitky.streamadapter.server.netty;

import cc.bitky.streamadapter.base.BasePresenter;
import cc.bitky.streamadapter.base.BaseView;
import cc.bitky.streamadapter.util.listener.FinishSuccessfulListener;
import cc.bitky.streamadapter.util.listener.LaunchSuccessfulListener;

public class ServerContract {

  public interface IServerView extends BaseView<IServerPresenter> {
    void setLaunchSuccessfulListener( LaunchSuccessfulListener successfulListener);
    void setFinishSuccessfulListener( FinishSuccessfulListener successfulListener);

    void start();

    void shutdown();
  }

  interface IServerPresenter extends BasePresenter {

  }
}
