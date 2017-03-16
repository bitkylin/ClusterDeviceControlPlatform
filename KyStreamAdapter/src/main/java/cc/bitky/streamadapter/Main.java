package cc.bitky.streamadapter;

import cc.bitky.streamadapter.server.view.ServerShow;
import java.util.Scanner;

public class Main {
  private static boolean isContinuing = true;
  private static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    keyIn();
  }

  private static void keyIn() {

    while (isContinuing) {
      welcomeTo();
      String inputMsg = scanner.nextLine();
      switch (inputMsg) {
        case "server":
          ServerShow.newInstance().startServer(scanner);
          return;

        case "exit":
          if (ServerShow.isRunning()) {
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
    System.out.println("  exit: 关闭程序");
    System.out.print("请输入: ");
  }
}
