package cc.bitky.clustermanage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.bitky.clustermanage.netty.NettyClient;
import cc.bitky.clustermanage.view.MainView;

public class NettyLauncher {
    private static Logger logger = LoggerFactory.getLogger(NettyLauncher.class);
    private static NettyLauncher nettyLauncher;
    private NettyClient nettyClient;

    private NettyLauncher() {
    }

    public static NettyLauncher getInstance() {
        if (nettyLauncher == null) nettyLauncher = new NettyLauncher();
        return nettyLauncher;
    }

    public void start(String hostName, int port) {
        nettyClient = new NettyClient();
        nettyClient.setLaunchSuccessfulListener(isSuccess -> {
            MainView.getInstance().setLabelConnStatus(isSuccess);
        });
        nettyClient.setFinishSuccessfulListener(isSuccess -> {
            logger.info("客户端优雅关闭成功");
            System.exit(0);
        });
        nettyClient.start(hostName, port);
    }

    void shutdown() {
        if (nettyClient != null)
            nettyClient.shutdown();
    }
}
