package cc.bitky.clusterdeviceplatform.server.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.server.db.DbPresenter;

@Service
public class ServerWebProcessor {

    private final ServerCenterProcessor centerProcessor;
    private final DbPresenter dbPresenter;

    @Autowired
    public ServerWebProcessor(ServerCenterProcessor centerProcessor, DbPresenter dbPresenter) {
        this.centerProcessor = centerProcessor;
        this.dbPresenter = dbPresenter;
    }

    public DbPresenter getDbPresenter() {
        return dbPresenter;
    }

    public boolean sendMessageGrouped(BaseMsg message) {
        return centerProcessor.sendMessageGrouped(message);
    }
}
