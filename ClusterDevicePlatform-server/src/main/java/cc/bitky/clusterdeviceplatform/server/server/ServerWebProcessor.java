package cc.bitky.clusterdeviceplatform.server.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.server.db.DbPresenter;

@Service
public class ServerWebProcessor {

    private final ServerCenterProcessor centerProcessor;


    @Autowired
    public ServerWebProcessor(ServerCenterProcessor centerProcessor) {
        this.centerProcessor = centerProcessor;
    }

    public ServerTcpProcessor getTcpProcessor() {
        return centerProcessor.getTcpProcessor();
    }

    public DbPresenter getDbPresenter() {
        return centerProcessor.getDbPresenter();
    }

    public boolean sendMessageGrouped(BaseMsg message) {
        return centerProcessor.sendMessageGrouped(message);
    }
}
