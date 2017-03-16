package cc.bitky.streamadapter.util.listener;

@FunctionalInterface
public interface FinishSuccessfulListener {
  void onSuccess(boolean isSuccess);
}
