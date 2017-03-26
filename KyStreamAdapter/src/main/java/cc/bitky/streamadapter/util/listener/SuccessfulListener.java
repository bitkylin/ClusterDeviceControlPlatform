package cc.bitky.streamadapter.util.listener;

@FunctionalInterface
public interface SuccessfulListener {
  void onSuccess(boolean isSuccess);
}
