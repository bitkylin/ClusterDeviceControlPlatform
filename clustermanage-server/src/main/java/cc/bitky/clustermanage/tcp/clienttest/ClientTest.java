package cc.bitky.clustermanage.tcp.clienttest;

import cc.bitky.clustermanage.server.bean.KyMessageHandler;
import cc.bitky.clustermanage.server.message.IMessage;
import cc.bitky.clustermanage.server.message.MsgChargeStatus;
import cc.bitky.clustermanage.server.message.MsgHeartBeat;
import cc.bitky.clustermanage.tcp.server.NettyMain;
import cc.bitky.clustermanage.tcp.util.enumky.ChargeStatusEnum;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientTest {
  private final KyMessageHandler kyMessageHandler;
  private NettyMain nettyMain;

  @Autowired
  public ClientTest(KyMessageHandler kyMessageHandler) {
    this.kyMessageHandler = kyMessageHandler;
  }

  public void startClient(NettyMain nettyMain, Scanner scanner) {
    this.nettyMain = nettyMain;
    keyIn(scanner);
  }

  private void keyIn(Scanner scanner) {
    while (true) {
      welcomeTo();
      String inputMsg = scanner.next();
      switch (inputMsg) {

        case "hb":
          int groupId = scanner.nextInt();
          IMessage hb = new MsgHeartBeat(groupId);
          kyMessageHandler.handle(hb);
          break;

        case "css":
          int groupId2 = scanner.nextInt();
          int boxId2 = scanner.nextInt();
          int status = scanner.nextInt();
          switch (status) {
            case 0:
            case 1:
            case 2:
            case 3:
              break;
            default:
              status = ChargeStatusEnum.CRASH;
              break;
          }
          IMessage css = new MsgChargeStatus(groupId2, boxId2, status);
          kyMessageHandler.handle(css);
          break;

        case "return":
          System.out.println("返回主菜单中...");
          new Thread(() -> nettyMain.run());

          return;

        case "no":
          return;
      }
    }
  }

  private void welcomeTo() {
    System.out.println("\n* * * * *");
    System.out.println("测试客户端: 请选择想要进行的操作");
    System.out.println("  hb「group」: 心跳包");
    System.out.println("  css「group」「box」「status」: 改变状态");
    System.out.println("  return: 返回主菜单");
    System.out.print("请输入: ");
  }
}
