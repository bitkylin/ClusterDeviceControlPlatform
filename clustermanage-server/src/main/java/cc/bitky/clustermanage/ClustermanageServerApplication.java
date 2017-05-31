package cc.bitky.clustermanage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import cc.bitky.clustermanage.global.ExSetting;
import cc.bitky.clustermanage.global.ServerSetting;

@SpringBootApplication
public class ClusterManageServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ClusterManageServerApplication.class);

    public static void main(String[] args) {
        initSetting();
        SpringApplication.run(ClusterManageServerApplication.class, args);
        logger.info("『bitky.cc』「" + ServerSetting.VERSION + "」");
    }

    private static void initSetting() {
        ExSetting exSetting = null;
        try {
            String readStr = new String(Files.readAllBytes(Paths.get(ServerSetting.CONFIG_FILE_PATH)), StandardCharsets.UTF_8);
            exSetting = JSON.parseObject(readStr, ExSetting.class);
        } catch (IOException e) {
            logger.info("「外部配置文件」未能读取到配置文件");
        } catch (JSONException e) {
            logger.info("「外部配置文件」反序列化失败");
        }
        if (exSetting != null) {
            logger.info("「外部配置文件」外部配置读取成功");
            ServerSetting.HOST = exSetting.数据库服务器的主机名或IP;
            ServerSetting.FRAME_SEND_INTERVAL = exSetting.帧发送间隔;
            ServerSetting.DEPLOY_REMAIN_CHARGE_TIMES = exSetting.部署剩余充电次数阈值;
            ServerSetting.DEFAULT_EMPLOYEE_CARD_NUMBER = exSetting.员工默认卡号;
            ServerSetting.DEFAULT_EMPLOYEE_NAME = exSetting.员工默认姓名;
            ServerSetting.DEFAULT_EMPLOYEE_DEPARTMENT = exSetting.员工默认部门;
        }
    }
}
