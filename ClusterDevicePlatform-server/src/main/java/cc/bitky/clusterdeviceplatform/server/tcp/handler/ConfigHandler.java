package cc.bitky.clusterdeviceplatform.server.tcp.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.server.tcp.TcpPresenter;
import cc.bitky.clusterdeviceplatform.server.tcp.repo.TcpRepository;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Service
@ChannelHandler.Sharable
public class ConfigHandler extends ChannelInboundHandlerAdapter {

    private final TcpPresenter tcpPresenter;
    private final Logger logger = LoggerFactory.getLogger(TcpRepository.class);

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
        logger.info(cause.getLocalizedMessage());
    }
}
