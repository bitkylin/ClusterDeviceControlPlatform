package cc.bitky.clusterdeviceplatform.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.InetAddress;

import cc.bitky.clusterdeviceplatform.client.config.CommSetting;
import cc.bitky.clusterdeviceplatform.client.config.DeviceSetting;

@SpringBootApplication
public class ClientApplication {
    private static Logger logger = LoggerFactory.getLogger(ClientApplication.class);

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                String ip = args[0];
                InetAddress inetAddress = InetAddress.getByName(ip);
                if (!inetAddress.isReachable(CommSetting.ACCESSIBLE_CHANNEL_REPLY_INTERVAL)) {
                    System.out.println("输入的第一个参数有误");
                    return;
                }

                CommSetting.SERVER_HOSTNAME = ip;
            }

            if (args.length > 1) {
                DeviceSetting.MAX_GROUP_ID = Integer.parseInt(args[1]);
            }
        } catch (IOException var3) {
            System.out.println("指定的服务器连接超时！");
            return;
        } catch (NumberFormatException var4) {
            System.out.println("输入的第二个参数有误，请检查！");
        }

        System.out.println("服务器地址: " + CommSetting.SERVER_HOSTNAME + ", 最大设备组数量: " + DeviceSetting.MAX_GROUP_ID);
        SpringApplication.run(ClientApplication.class, args);
    }
}
