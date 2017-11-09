package cc.bitky.clusterdeviceplatform.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Scanner;

import cc.bitky.clusterdeviceplatform.client.netty.NettyClient;

@Service
public class KyRun implements CommandLineRunner {

    private final NettyClient nettyClient;

    @Autowired
    public KyRun(NettyClient nettyClient) {
        this.nettyClient = nettyClient;
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("请输入要启用的客户端编号：");
            int index = scanner.nextInt();
            nettyClient.start(index);
        }
    }
}
