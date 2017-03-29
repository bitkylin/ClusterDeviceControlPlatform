package cc.bitky.clustermanage.tcp.server;

import cc.bitky.clustermanage.tcp.clienttest.ClientTest;
import cc.bitky.clustermanage.tcp.server.view.NettyServerShow;
import java.util.Scanner;

public class NettyMain {
  private static boolean isContinuing = true;
  private static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    keyIn();
  }

  private static void keyIn() {

    while (isContinuing) {
      welcomeTo();
      String inputMsg = scanner.next();
      switch (inputMsg) {
        case "server":
          NettyServerShow.newInstance().startServer(scanner);
          return;
        case "client":
          ClientTest.newInstance().startClient(scanner);
          return;
        case "exit":
          if (NettyServerShow.isRunning()) {
            System.out.println("警告: 请先关闭服务器！");
          } else {
            isContinuing = false;
          }
          break;
      }
    }
  }

  private static void welcomeTo() {
    System.out.println("\n* * * * *");
    System.out.println("主菜单: 请选择想要进行的操作");
    System.out.println("  server: 开启服务器");
    System.out.println("  client: 开启测试客户端");
    System.out.println("  exit: 关闭程序");
    System.out.print("请输入: ");
  }
}
