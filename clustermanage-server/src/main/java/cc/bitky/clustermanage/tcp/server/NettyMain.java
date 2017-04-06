package cc.bitky.clustermanage.tcp.server;

import cc.bitky.clustermanage.tcp.clienttest.ClientTest;
import cc.bitky.clustermanage.tcp.server.view.NettyServerShow;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class NettyMain implements CommandLineRunner {
  private static boolean isContinuing = true;
  private static Scanner scanner = new Scanner(System.in);
  private final ClientTest clientTest;
  private final NettyServerShow nettyServerShow;

  @Autowired
  public NettyMain(ClientTest clientTest, NettyServerShow nettyServerShow) {
    this.clientTest = clientTest;
    this.nettyServerShow = nettyServerShow;
  }

  private static void welcomeTo() {
    System.out.println("\n* * * * *");
    System.out.println("主菜单: 请选择想要进行的操作");
    System.out.println("  server: 开启服务器");
    System.out.println("  client: 开启测试客户端");
    System.out.println("  exit: 关闭程序");
    System.out.print("请输入: ");
  }

  private void keyIn() {

    while (isContinuing) {
      welcomeTo();
      String inputMsg = scanner.next();
      switch (inputMsg) {
        case "server":
          new Thread(() -> nettyServerShow.startServer(this, scanner));
          return;
        case "client":
        //  new Thread(() -> clientTest.startClient(this, scanner));
          clientTest.startClient(this, scanner);
          return;
        case "exit":
          if (nettyServerShow.isRunning()) {
            System.out.println("警告: 请先关闭服务器！");
          } else {
            isContinuing = false;
          }
          break;
      }
    }
  }

  @Override
  public void run(String... args) {
   // new Thread(this::keyIn);
    keyIn();
  }
}
