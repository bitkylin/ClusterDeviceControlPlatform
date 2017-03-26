package cc.bitky.streamadapter.server.netty;

import cc.bitky.streamadapter.base.BasePresenter;
import cc.bitky.streamadapter.base.BaseView;
import cc.bitky.streamadapter.util.listener.SuccessfulListener;

public class ServerContract {

  public interface IServerView extends BaseView<IServerPresenter> {
    void setLaunchSuccessfulListener( SuccessfulListener successfulListener);
    void setFinishSuccessfulListener( SuccessfulListener successfulListener);

    void start();

    void shutdown();
  }

  interface IServerPresenter extends BasePresenter {

  }
}
