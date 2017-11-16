package cc.bitky.clusterdeviceplatform.server.server;

import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.messageutils.define.BaseMsg;

@Service
public class ServerCenterProcessor {

    private ServerTcpProcessor tcpProcessor;

    public boolean sendMessage(BaseMsg message) {
        return tcpProcessor.sendMessage(message);
    }

    public void setTcpProcessor(ServerTcpProcessor tcpProcessor) {
        this.tcpProcessor = tcpProcessor;
    }
}
