package cc.bitky.clustermanage.tcp.server.channelhandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cc.bitky.clustermanage.server.bean.ServerTcpMessageHandler;
import cc.bitky.clustermanage.server.message.base.BaseTcpResponseMsg;
import cc.bitky.clustermanage.server.message.base.IMessage;
import cc.bitky.clustermanage.server.message.tcp.TcpMsgResponseStatus;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@Service
@ChannelHandler.Sharable
public class ParsedMessageInBoundHandler extends SimpleChannelInboundHandler<IMessage> {
    private ServerChannelInitializer serverChannelInitializer;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void setServerChannelInitializer(ServerChannelInitializer serverChannelInitializer) {
        this.serverChannelInitializer = serverChannelInitializer;
    }

    ServerTcpMessageHandler getServerTcpMessageHandler() {
        return serverChannelInitializer.getServerTcpMessageHandler();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, IMessage msg) {
        //检测捕获到的帧异常「限定 ID 范围：设备组『1 - 100』；设备『1 - 100』」
        if (msg.getDeviceId() != -2 && (msg.getGroupId() <= 0 || msg.getGroupId() > 100 || msg.getDeviceId() <= 0 || msg.getDeviceId() > 100))
            return;

        //将常规回复帧信息传入「常规回复信息处理方法」
        if (msg.getMsgId() > 0x40 && msg.getMsgId() <= 0x4F) {
            getServerTcpMessageHandler().handleTcpResponseMsg((BaseTcpResponseMsg) msg);
            return;
        }

        if (msg.getMsgId() >= ((byte) 0x80) && msg.getMsgId() <= ((byte) 0x8F)) {
            getServerTcpMessageHandler().handleTcpResponseMsg((BaseTcpResponseMsg) msg);
            return;
        }

        if (msg.getMsgId() == 0x40) {
            getServerTcpMessageHandler().handleResDeviceStatus((TcpMsgResponseStatus) msg);
            return;
        }

        //将初始化帧信息传入「初始化信息处理方法」
        byte a0 = (byte) 0xA0;
        byte af = (byte) 0xAF;
        if (msg.getMsgId() >= a0 && msg.getMsgId() <= af) {
            getServerTcpMessageHandler().handleTcpInitMsg(msg);
            return;
        }

        //将其余功能帧信息传入「功能信息处理方法」
        getServerTcpMessageHandler().handleTcpMsg(msg);
    }
}
