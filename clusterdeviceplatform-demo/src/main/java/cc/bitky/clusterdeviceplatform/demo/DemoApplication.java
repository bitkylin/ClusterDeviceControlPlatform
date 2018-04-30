package cc.bitky.clusterdeviceplatform.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cc.bitky.clusterdeviceplatform.messageutils.MsgProcessor;

@SpringBootApplication
public class DemoApplication {

    private static MsgProcessor msgProcessor;

    public static void main(String[] args) {
        msgProcessor = MsgProcessor.getInstance();
        SpringApplication.run(DemoApplication.class, args);
    }
}
