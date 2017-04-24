package cc.bitky.clustermanage.tcp.util.listener;

@FunctionalInterface
public interface SuccessfulListener {
    void onSuccess(boolean isSuccess);
}
