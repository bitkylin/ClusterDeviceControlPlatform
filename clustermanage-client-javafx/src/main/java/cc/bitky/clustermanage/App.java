package cc.bitky.clustermanage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

import cc.bitky.clustermanage.netty.NettyServer;
import cc.bitky.clustermanage.netty.handler.KyChannelInitializer;
import cc.bitky.clustermanage.netty.message.MsgType;
import cc.bitky.clustermanage.netty.message.base.BaseTcpResponseMsg;
import cc.bitky.clustermanage.netty.message.base.IMessage;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgInitResponseCardNumber;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseDeviceStatus;
import cc.bitky.clustermanage.utils.ChargeStatusEnum;

public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);
    private Scanner scanner = new Scanner(System.in);
    private NettyServer nettyServer;

    public App() {
    }

    public static void main(String[] args) {

        logger.debug("debug");
        logger.info("info");
        // new App().start();

    }

    private void welcomeTo() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\n* * * * *");
        System.out.println("测试客户端: 请选择想要进行的操作");
        System.out.println("    cs「group」「box」「status」: 改变状态");
        System.out.println("  card「group」「box」「number」: 上传卡号");
        System.out.println(" re[x]「group」「box」「number」: 帧类型");
        System.out.println("  exit: 返回主菜单");
        System.out.print("请输入: ");
    }

    public void start() {
        nettyServer = new NettyServer();
        nettyServer.setLaunchSuccessfulListener(isSuccess -> keyIn());
        nettyServer.setFinishSuccessfulListener(isSuccess -> System.out.println("客户端优雅关闭成功"));
        nettyServer.start();
    }

    private void keyIn() {

        while (true) {
            welcomeTo();
            String inputMsg = scanner.next();
            IMessage message;
            int groupId;
            int boxId;
            int status;
            switch (inputMsg) {
                case "cs":
                    groupId = scanner.nextInt();
                    boxId = scanner.nextInt();
                    status = scanner.nextInt();
                    if (status < 0 || status > 6) {
                        status = ChargeStatusEnum.CRASH;
                    }
                    message = new TcpMsgResponseDeviceStatus(groupId, boxId, status);
                    break;
                case "card":
                    groupId = scanner.nextInt();
                    boxId = scanner.nextInt();
                    long cardNum = scanner.nextLong();
                    message = new TcpMsgInitResponseCardNumber(groupId, boxId, cardNum);
                    break;
                case "rest":
                    message = buildResMsg(MsgType.DEVICE_RESPONSE_REMAIN_CHARGE_TIMES);
                    break;
                case "resb":
                    message = buildResMsg(MsgType.DEVICE_RESPONSE_DEVICE_ID);
                    break;
                case "resn":
                    message = buildResMsg(MsgType.DEVICE_RESPONSE_EMPLOYEE_NAME);
                    break;
                case "resd":
                    message = buildResMsg(MsgType.DEVICE_RESPONSE_EMPLOYEE_DEPARTMENT_1);
                    break;
                case "rese":
                    message = buildResMsg(MsgType.DEVICE_RESPONSE_EMPLOYEE_CARD_NUMBER);
                    break;
                case "resl":
                    message = buildResMsg(MsgType.DEVICE_RESPONSE_REMOTE_UNLOCK);
                    break;
                case "resf":
                    message = buildResMsg(MsgType.DEVICE_RESPONSE_FREE_CARD_NUMBER);
                    break;
                case "err":
                    message = buildResMsg(0);
                    break;
                case "exit":
                    nettyServer.shutdown();
                    return;
                default:
                    continue;
            }
            if (message != null) {
                KyChannelInitializer.newInstance().getPipeline().write(message);
            }
        }
    }

    private IMessage buildResMsg(int msgId) {
        int groupId = scanner.nextInt();
        int boxId = scanner.nextInt();
        int status = scanner.nextInt();
        IMessage message = new BaseTcpResponseMsg(groupId, boxId, status);
        message.setMsgId(msgId);
        return message;
    }
}
