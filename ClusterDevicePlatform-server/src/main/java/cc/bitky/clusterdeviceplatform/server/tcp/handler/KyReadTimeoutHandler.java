package cc.bitky.clusterdeviceplatform.server.tcp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.handler.timeout.ReadTimeoutHandler;

public class KyReadTimeoutHandler extends ReadTimeoutHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public KyReadTimeoutHandler(int timeoutSeconds) {
        super(timeoutSeconds);
    }
}
