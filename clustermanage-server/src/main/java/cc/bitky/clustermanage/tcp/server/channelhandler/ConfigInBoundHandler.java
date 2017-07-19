package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

@Service
@ChannelHandler.Sharable
public class ConfigInBoundHandler extends ChannelInboundHandlerAdapter {
    private  ServerChannelInitializer serverChannelInitializer;
    private Logger logger = LoggerFactory.getLogger(ConfigInBoundHandler.class);
    private int i = 0;

    void setServerChannelInitializer(ServerChannelInitializer serverChannelInitializer) {
        this.serverChannelInitializer = serverChannelInitializer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        logger.info("通道重置");
        List<ChannelPipeline> channelPipelines = serverChannelInitializer.getServerTcpMessageHandler().getSendingMsgRepo().getChannelPipelines();
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
