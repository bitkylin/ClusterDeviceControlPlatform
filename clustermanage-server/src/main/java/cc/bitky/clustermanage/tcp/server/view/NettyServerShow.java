package cc.bitky.clustermanage.tcp.server.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Scanner;

import cc.bitky.clustermanage.tcp.server.netty.NettyServer;
import cc.bitky.clustermanage.tcp.server.netty.NettyServerContract.IServerView;

@Service
public class NettyServerShow implements CommandLineRunner {
    private final IServerView nettyServer;
    Logger logger = LoggerFactory.getLogger(getClass());
    private boolean running = false;
    // private Scanner scanner;
    // private NettyMain nettyMain;

    @Autowired
    public NettyServerShow(NettyServer nettyServer) {
        this.nettyServer = nettyServer;
    }

    public boolean isRunning() {
        return running;
    }

    public void startServer(Scanner scanner) {
        //    this.nettyMain = nettyMain;
        //  this.scanner = scanner;
        if (!running) {
            logger.info("服务器正在启动...");
            nettyServer.setLaunchSuccessfulListener(isSuccess -> {
                if (isSuccess) {
                    logger.info("端口绑定成功");
                    // newThreadKeyIn();
                    //       newThreadToMain();
                } else {
                    logger.warn("端口绑定出错");
                    running = false;
                    logger.warn("服务器启动失败！");
                }
            });

            nettyServer.start();
            running = true;
        } else {
            //     newThreadKeyIn();
        }
    }

    private void keyIn(Scanner scanner) {
        while (true) {
            welcomeTo();
            String inputMsg = scanner.next();
            switch (inputMsg) {

                case "return":
                    logger.info("返回主菜单中...");
                    //       newThreadToMain();
                    return;

                case "stop":
                    logger.info("服务器正在结束...");
                    stopServer();
                    return;

                case "no":
                    return;
            }
        }
    }

    private void stopServer() {
        nettyServer.setFinishSuccessfulListener(isSuccess -> {
            if (isSuccess) {
                logger.info("服务器优雅关闭成功");
                running = false;
                nettyServer.setFinishSuccessfulListener(null);
                nettyServer.setLaunchSuccessfulListener(null);
                //      newThreadToMain();
            } else {
                logger.warn("服务器优雅关闭失败");
                running = true;
                //      newThreadKeyIn();
            }
        });
        nettyServer.shutdown();
    }

    //private void newThreadToMain() {
    //  new Thread(() -> {
    //    nettyMain.run();
    //  }).start();
    //}

    //private void newThreadKeyIn() {
    //  new Thread(() -> {
    //    keyIn(scanner);
    //  }).start();
    //}

    private void welcomeTo() {
        System.out.println("\n* * * * *");
        System.out.println("服务器: 请选择想要进行的操作");
        System.out.println("  return: 返回主菜单");
        System.out.println("  stop: 停止服务器");
        System.out.print("请输入: ");
    }

    @Override
    public void run(String... strings) throws Exception {
        startServer(new Scanner(System.in));
    }
}
