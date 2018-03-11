package cc.bitky.clusterdeviceplatform.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import cc.bitky.clusterdeviceplatform.demo.config.DbSetting;
import cc.bitky.clusterdeviceplatform.demo.server.statistic.utils.IpUtil;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
        DbSetting.MONGODB_IP = IpUtil.getIP(DbSetting.MONGODB_HOST)[0];
    }
}
