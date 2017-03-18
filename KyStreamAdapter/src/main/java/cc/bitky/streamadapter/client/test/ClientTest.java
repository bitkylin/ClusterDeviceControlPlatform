package cc.bitky.streamadapter.client.test;

import cc.bitky.streamadapter.Main;
import cc.bitky.streamadapter.server.dynamickeeper.Maintainer;
import cc.bitky.streamadapter.util.bean.message.ChargeStatus;
import cc.bitky.streamadapter.util.bean.message.HeartBeat;
import cc.bitky.streamadapter.util.bean.message.IMessage;
import cc.bitky.streamadapter.util.enumky.ChargeStatusEnum;
import java.util.Scanner;

public class ClientTest {
  private static ClientTest clientTest = null;

  public static ClientTest newInstance() {
    if (clientTest == null) {
      clientTest = new ClientTest();
    }
    return clientTest;
  }

  public void startClient(Scanner scanner) {
    keyIn(scanner);
  }

  private void keyIn(Scanner scanner) {
    while (true) {
      welcomeTo();
      String inputMsg = scanner.next();
      switch (inputMsg) {

        case "hb":
          int groupId = scanner.nextInt();
          IMessage hb = new HeartBeat(groupId);
          Maintainer.process(hb);
          break;

        case "css":
          int groupId2 = scanner.nextInt();
          int boxId2 = scanner.nextInt();
          ChargeStatusEnum status = ChargeStatusEnum.CRASH;
          switch (scanner.nextInt()) {
            case 0:
              status = ChargeStatusEnum.USING;
              break;
            case 1:
              status = ChargeStatusEnum.CHARGING;
              break;
            case 2:
              status = ChargeStatusEnum.FULL;
              break;
            case 3:
              status = ChargeStatusEnum.CRASH;
              break;
          }
          IMessage css = new ChargeStatus(groupId2, boxId2, status);
          Maintainer.process(css);
          break;

        case "return":
          System.out.println("返回主菜单中...");
          Main.main(new String[0]);
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
