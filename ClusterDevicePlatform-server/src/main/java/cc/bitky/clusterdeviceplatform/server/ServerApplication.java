package cc.bitky.clusterdeviceplatform.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import cc.bitky.clusterdeviceplatform.server.config.CommSetting;
import cc.bitky.clusterdeviceplatform.server.config.DbSetting;
import cc.bitky.clusterdeviceplatform.server.config.LocalProfile;
import cc.bitky.clusterdeviceplatform.server.config.ServerSetting;
import cc.bitky.clusterdeviceplatform.server.server.statistic.utils.IpUtil;

@SpringBootApplication
public class ServerApplication {
    private static Logger logger = LoggerFactory.getLogger(ServerApplication.class);

    private static void print(String str) {
        logger.info(str);
    }

    private static void printWarn(String str) {
        logger.warn(str);
    }

    public static void main(String[] args) {
        long l1 = System.currentTimeMillis();
        if (initSetting() && dataBaseReachable()) {
            long l2 = System.currentTimeMillis();
            print("服务器预检查完成，共耗时 " + (l2 - l1) + " ms");
            SpringApplication.run(ServerApplication.class, args);
            print("服务器已正常启动「" + ServerSetting.VERSION + "」");
        } else {
            printWarn("服务器启动失败「" + ServerSetting.VERSION + "」");
        }
    }

    /**
     * 数据库是否可到达
     *
     * @return 返回可到达
     */
    private static boolean dataBaseReachable() {
        try {
            InetAddress inetAddress = InetAddress.getByName(DbSetting.MONGODB_HOST);
            if (inetAddress.isReachable(CommSetting.ACCESSIBLE_CHANNEL_REPLY_INTERVAL)) {
                Socket s = new Socket();
                s.connect(new InetSocketAddress(inetAddress, DbSetting.MONGODB_PORT));
                s.close();
            } else {
                printWarn("数据库服务器不可达");
                return false;
            }

        } catch (IOException e) {
            printWarn("数据库异常，请检查数据库是否成功启动");
            return false;
        }
        try {
            Socket s1 = new Socket();
            s1.connect(new InetSocketAddress(InetAddress.getLocalHost(), ServerSetting.SERVER_WEB_PORT));
            s1.close();
            Socket s2 = new Socket();
            s1.connect(new InetSocketAddress(InetAddress.getLocalHost(), ServerSetting.SERVER_TCP_PORT));
            s1.close();
            printWarn("本机相关端口号被占用，请检查这两个端口号是否可用：" + ServerSetting.SERVER_WEB_PORT + ", " + ServerSetting.SERVER_TCP_PORT);
            return false;
        } catch (IOException e) {
            return true;
        }
    }

    /**
     * 读取外部配置文件并初始化服务器的配置
     *
     * @return 读取文件并初始化成功
     */

    private static boolean initSetting() {
        LocalProfile localProfile = null;
        try {
            String strings = new String(Files.readAllBytes(Paths.get(ServerSetting.CONFIG_FILE_PATH)), StandardCharsets.UTF_8);
            localProfile = JSON.parseObject(strings, LocalProfile.class);
        } catch (IOException e) {
            printWarn("「外部配置文件」未能读取到配置文件");
        } catch (JSONException e) {
            printWarn("「外部配置文件」反序列化失败");
        }
        if (localProfile != null) {
            DbSetting.MONGODB_HOST = trimProperty(localProfile.数据库服务器的主机名或IP);
            DbSetting.MONGODB_IP = IpUtil.getIP(DbSetting.MONGODB_HOST)[0];
            CommSetting.FRAME_SEND_INTERVAL = localProfile.帧发送间隔;
            CommSetting.DEPLOY_REMAIN_CHARGE_TIMES = localProfile.部署剩余充电次数阈值 >= CommSetting.REMAIN_CHARGE_TIMES_CLEAR ? CommSetting.REMAIN_CHARGE_TIMES_CLEAR - 1 : localProfile.部署剩余充电次数阈值;
            DbSetting.DEFAULT_EMPLOYEE_CARD_NUMBER = trimProperty(localProfile.员工默认卡号);
            DbSetting.DEFAULT_EMPLOYEE_NAME = trimProperty(localProfile.员工默认姓名);
            DbSetting.DEFAULT_EMPLOYEE_DEPARTMENT = trimProperty(localProfile.员工默认部门);
            CommSetting.NO_RESPONSE_INTERVAL = localProfile.通道未响应时间;
            CommSetting.AUTO_REPEAT_REQUEST_TIMES = localProfile.检错重发最大重复次数;
            CommSetting.NO_RESPONSE_MONITOR = localProfile.通道无响应监测;
            CommSetting.DEPLOY_MSG_NEED_REPLY = localProfile.帧送达监测;
            ServerSetting.DEBUG = localProfile.调试模式;
            ServerSetting.WEB_RANDOM_DEBUG = localProfile.随机Web数据模式;
            DbSetting.AUTHENTICATION_STATUS = localProfile.数据库认证模式;
            ServerSetting.SERVER_TCP_PORT = localProfile.服务器端口号;
            DbSetting.DATABASE_USERNAME = trimProperty(localProfile.数据库用户名);
            DbSetting.DATABASE_PASSWORD = trimProperty(localProfile.数据库密码);
            return true;
        }
        printWarn("外部配置文件读取错误，请使用「服务器预设置」软件进行设置");
        return false;
    }

    private static String trimProperty(String text) {
        if (text == null) {
            return "";
        } else {
            return text.trim();
        }
    }
}
