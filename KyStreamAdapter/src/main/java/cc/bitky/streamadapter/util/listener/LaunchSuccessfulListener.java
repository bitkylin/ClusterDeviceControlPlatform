package cc.bitky.streamadapter.util.listener;

/**
 * Created by bitky on 2017/3/13.
 */
@FunctionalInterface
public interface LaunchSuccessfulListener {
  void onSuccess(boolean isSuccess);
}
