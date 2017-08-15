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
        if (initSetting()) {
            SpringApplication.run(ClusterManageServerApplication.class, args);
        } else {
            logger.error("外部配置文件读取错误，请使用「服务器预设置」软件进行设置");
        }
        logger.info("『bitky.cc』「" + ServerSetting.VERSION + "」");
    }

    /**
     * 读取外部配置文件并初始化服务器的配置
     *
     * @return 读取文件并初始化成功
     */
    private static boolean initSetting() {
        ExSetting exSetting = null;
        try {
            String strings = new String(Files.readAllBytes(Paths.get(ServerSetting.CONFIG_FILE_PATH)), StandardCharsets.UTF_8);
            exSetting = JSON.parseObject(strings, ExSetting.class);
        } catch (IOException e) {
            logger.warn("「外部配置文件」未能读取到配置文件");
        } catch (JSONException e) {
            logger.warn("「外部配置文件」反序列化失败");
        }
        if (exSetting != null) {
            logger.info("「外部配置文件」外部配置读取成功");
            ServerSetting.HOST = exSetting.数据库服务器的主机名或IP;
            ServerSetting.FRAME_SEND_INTERVAL = exSetting.帧发送间隔;
            ServerSetting.DEPLOY_REMAIN_CHARGE_TIMES = exSetting.部署剩余充电次数阈值;
            ServerSetting.DEFAULT_EMPLOYEE_CARD_NUMBER = exSetting.员工默认卡号;
            ServerSetting.DEFAULT_EMPLOYEE_NAME = exSetting.员工默认姓名;
            ServerSetting.DEFAULT_EMPLOYEE_DEPARTMENT = exSetting.员工默认部门;
            ServerSetting.AUTO_REPEAT_REQUEST_TIMES = exSetting.检错重发最大重复次数;
            ServerSetting.DEVICE_INIT_CHARGE_TIMES = exSetting.初始充电次数;
            return true;
        }
        return false;
    }
}
