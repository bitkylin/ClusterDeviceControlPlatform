package cc.bitky.clustermanage.tcp.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import cc.bitky.clustermanage.tcp.server.netty.NettyServer;
import cc.bitky.clustermanage.tcp.server.netty.NettyServerContract.IServerView;

@Service
public class NettyServerShow implements CommandLineRunner {
    private final IServerView nettyServer;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private boolean running = false;

    @Autowired
    public NettyServerShow(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    private void startServer() {
        if (!running) {
            logger.info("「Netty」服务器正在启动...");
            nettyServer.setLaunchSuccessfulListener(isSuccess -> {
                if (isSuccess) {
                    logger.info("「Netty」端口绑定成功");
                    // newThreadKeyIn();
                    //       newThreadToMain();
                } else {
                    logger.warn("「Netty」端口绑定出错");
                    running = false;
                    logger.warn("「Netty」服务器启动失败！");
                }
            });
            nettyServer.start();
            running = true;
        }
    }

    private void stopServer() {
        nettyServer.setFinishSuccessfulListener(isSuccess -> {
            if (isSuccess) {
                logger.info("「Netty」服务器优雅关闭成功");
                running = false;
                nettyServer.setFinishSuccessfulListener(null);
                nettyServer.setLaunchSuccessfulListener(null);
            } else {
                logger.warn("「Netty」服务器优雅关闭失败");
                running = true;
            }
        });
        nettyServer.shutdown();
    }

    @Override
    public void run(String... strings) throws Exception {
        startServer();
    }
}
