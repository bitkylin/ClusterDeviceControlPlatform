package cc.bitky.clusterdeviceplatform.server.tcp.handler;

import cc.bitky.clusterdeviceplatform.server.tcp.TcpPresenter;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author limingliang
 */
@Slf4j
@Service
@ChannelHandler.Sharable
public class ConfigHandler extends ChannelDuplexHandler {

    private final TcpPresenter tcpPresenter;

    @Autowired
    public ConfigHandler(TcpPresenter tcpPresenter) {
        this.tcpPresenter = tcpPresenter;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        tcpPresenter.channelActive(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        tcpPresenter.channelInactiveCompleted(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("Channel抛出异常: " + cause.toString());
        if (cause instanceof ReadTimeoutException) {
            log.info("Channel未响应，正在断开");
            tcpPresenter.channelNoResponse(ctx.channel());
        }
    }
}
