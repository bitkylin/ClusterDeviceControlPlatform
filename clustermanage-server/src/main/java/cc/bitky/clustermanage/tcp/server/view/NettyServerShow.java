package cc.bitky.clustermanage.tcp.server.view;

import cc.bitky.clustermanage.tcp.server.NettyMain;
import cc.bitky.clustermanage.tcp.server.netty.NettyServer;
import cc.bitky.clustermanage.tcp.server.netty.NettyServerContract.IServerView;
import java.util.Scanner;

public class NettyServerShow {
  private static NettyServerShow view;
  private static boolean running = false;
  private IServerView serverView;
  private Scanner scanner;

  public static boolean isRunning() {
    return running;
  }

  public static NettyServerShow newInstance() {
    if (view == null) {

      view = new NettyServerShow();
    }
    return view;
  }

  public void startServer(Scanner scanner) {
    this.scanner = scanner;
    if (!running) {
      System.out.println("服务器正在启动...");
      serverView = new NettyServer();
      serverView.setLaunchSuccessfulListener(isSuccess -> {
        if (isSuccess) {
          System.out.println("端口绑定成功");
          // newThreadKeyIn();
          newThreadToMain();
        } else {
          System.out.println("端口绑定出错");
          running = false;
          System.out.println("服务器启动失败！");
        }
      });

      serverView.start();
      running = true;
    } else {
      newThreadKeyIn();
    }
  }

  private void keyIn(Scanner scanner) {
    while (true) {
      welcomeTo();
      String inputMsg = scanner.next();
      switch (inputMsg) {

        case "return":
          System.out.println("返回主菜单中...");
          newThreadToMain();
          return;

        case "stop":
          System.out.println("服务器正在结束...");
          stopServer();
          return;

        case "no":
          return;
      }
    }
  }

  private void stopServer() {
    serverView.setFinishSuccessfulListener(isSuccess -> {
      if (isSuccess) {
        System.out.println("服务器优雅关闭成功");
        running = false;
        serverView.setFinishSuccessfulListener(null);
        serverView.setLaunchSuccessfulListener(null);
        serverView = null;
        newThreadToMain();
      } else {
        System.out.println("服务器优雅关闭失败");
        running = true;
        newThreadKeyIn();
      }
    });
    serverView.shutdown();
  }

  private void newThreadToMain() {
    new Thread(() -> {
      NettyMain.main(new String[0]);
    }).start();
  }

  private void newThreadKeyIn() {
    new Thread(() -> {
      keyIn(scanner);
    }).start();
  }

  private void welcomeTo() {
    System.out.println("\n* * * * *");
    System.out.println("服务器: 请选择想要进行的操作");
    System.out.println("  return: 返回主菜单");
    System.out.println("  stop: 停止服务器");
    System.out.print("请输入: ");
  }
}
