package cc.bitky.clusterdeviceplatform.client.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.client.server.ServerTcpHandler;
import cc.bitky.clusterdeviceplatform.client.ui.view.UiLauncher;
import cc.bitky.clusterdeviceplatform.messageutils.define.BaseMsg;

@Service
public class UiPresenter implements CommandLineRunner {

    public final ServerTcpHandler server;

    @Autowired
    public UiPresenter(ServerTcpHandler server) {
        this.server = server;
    }

    @Override
    public void run(String... args) throws Exception {
        UiLauncher.runUi(this, args);
    }

    public void shutdown() {
        server.shutdown();
    }

    public void sendMessage(BaseMsg message) {
        server.sendMessage(message);
    }
}
