package cc.bitky.clustermanage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClustermanageServerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ClustermanageServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ClustermanageServerApplication.class, args);
        logger.info("『bitky.cc』「"+ServerSetting.VERSION+"」");
    }
}
