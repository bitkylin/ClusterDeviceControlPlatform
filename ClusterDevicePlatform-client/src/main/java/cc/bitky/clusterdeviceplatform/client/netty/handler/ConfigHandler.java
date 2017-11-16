package cc.bitky.clusterdeviceplatform.client.netty.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cc.bitky.clusterdeviceplatform.client.netty.TcpPresenter;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

@Service
@ChannelHandler.Sharable
public class ConfigHandler extends ChannelInboundHandlerAdapter {

    private final TcpPresenter tcpPresenter;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ConfigHandler(TcpPresenter tcpPresenter) {
        this.tcpPresenter = tcpPresenter;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Attribute<Integer> key = ctx.channel().attr(AttributeKey.valueOf("ID"));
        logger.info("Channel「" + key.get() + "」建立连接成功");
        tcpPresenter.channelActive(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Attribute<Integer> key = ctx.channel().attr(AttributeKey.valueOf("ID"));
        logger.info("Channel「" + key.get() + "」已经断开连接");
        tcpPresenter.channelInactive(key.get());
    }
}
