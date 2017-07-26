package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import cc.bitky.clustermanage.tcp.TcpMediator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

@Service
@ChannelHandler.Sharable
public class ConfigHandler extends ChannelInboundHandlerAdapter {
    private final TcpMediator tcpMediator;
    private Logger logger = LoggerFactory.getLogger(ConfigHandler.class);
    private int i = 0;

    @Autowired
    public ConfigHandler(TcpMediator tcpMediator) {
        this.tcpMediator = tcpMediator;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("通道重置");
        List<ChannelPipeline> channelPipelines = tcpMediator.getSendingMsgRepo().getChannelPipelines();
        channelPipelines.add(ctx.pipeline());
        channelPipelines.removeIf(channel -> {
            i++;
            if (channel == null || !channel.channel().isActive()) {
                logger.info("「" + i + "」" + "通道失效");
                return true;
            } else {
                logger.info("「" + i + "」" + "通道有效");
                return false;
            }
        });
        i = 0;
        logger.info("通道数量：" + channelPipelines.size());
    }
}
