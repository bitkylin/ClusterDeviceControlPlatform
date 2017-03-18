package cc.bitky.streamadapter.server.view;

import cc.bitky.streamadapter.Main;
import cc.bitky.streamadapter.server.netty.KyServer;
import cc.bitky.streamadapter.server.netty.ServerContract.IServerView;
import java.util.Scanner;

public class ServerShow {
  private static ServerShow view;
  private static boolean running = false;
  private IServerView serverView;
  private Scanner scanner;

  public static boolean isRunning() {
    return running;
  }

  public static ServerShow newInstance() {
    if (view == null) {

      view = new ServerShow();
    }
    return view;
  }

  public void startServer(Scanner scanner) {
    this.scanner = scanner;
    if (!running) {
      System.out.println("服务器正在启动...");
      serverView = new KyServer();
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
        running = false;
        System.out.println("服务器已停止运行！");
        serverView.setFinishSuccessfulListener(null);
        serverView.setLaunchSuccessfulListener(null);
        serverView = null;
        newThreadToMain();
      } else {
        running = true;
        System.out.println("服务器停止失败！");
        newThreadKeyIn();
      }
    });
    serverView.shutdown();
  }

  private void newThreadToMain() {
    new Thread(() -> {
      Main.main(new String[0]);
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
