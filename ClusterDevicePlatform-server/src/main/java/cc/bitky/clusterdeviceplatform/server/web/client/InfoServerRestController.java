package cc.bitky.clusterdeviceplatform.server.web.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

import cc.bitky.clusterdeviceplatform.server.config.DbSetting;
import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;

@RestController
@RequestMapping(value = "/info/server")
public class InfoServerRestController {

    private final ServerWebProcessor serverProcessor;

    @Autowired
    public InfoServerRestController(ServerWebProcessor serverProcessor) {
        this.serverProcessor = serverProcessor;
    }

    @GetMapping("/database/ip")
    public String obtainDatabaseIP() throws UnknownHostException {
        InetAddress address = InetAddress.getByName(DbSetting.mongodbHost);
        return address.getHostAddress();
    }

    @GetMapping("/database/auth")
    public DatabaseAuth obtainDatabaseAuth() {
        return new DatabaseAuth(DbSetting.databaseUsername, DbSetting.databasePassword);
    }

    /**
     * 获取服务器状态
     *
     * @return 服务器状态
     */
    @GetMapping("/status")
    public String obtainServerStatus() {
        return "success";
    }

    private class DatabaseAuth {
        private String username;
        private String password;

        private DatabaseAuth(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}
