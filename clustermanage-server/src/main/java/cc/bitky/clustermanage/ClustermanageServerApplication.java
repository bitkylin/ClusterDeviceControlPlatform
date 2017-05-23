package cc.bitky.clustermanage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClusterManageServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ClusterManageServerApplication.class);

    public static void main(String[] args) {
        JSON json = new JSONObject();
        SpringApplication.run(ClusterManageServerApplication.class, args);
        logger.info("『bitky.cc』「" + ServerSetting.VERSION + "」");
    }
}
