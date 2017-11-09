package cc.bitky.clusterdeviceplatform.server.tcp.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Service
public class ServerChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    private final FrameRecognitionInBoundHandler frameRecognitionInBoundHandler;
    private final ParsedMessageInBoundHandler parsedMessageInBoundHandler;
    private final MsgRecognitionOutBoundHandler recognitionOutBoundHandler;
    private final ConfigHandler configHandler;

    @Autowired
    public ServerChannelInitializer(FrameRecognitionInBoundHandler frameRecognitionInBoundHandler, ParsedMessageInBoundHandler parsedMessageInBoundHandler, MsgRecognitionOutBoundHandler recognitionOutBoundHandler, ConfigHandler configHandler) {
        this.frameRecognitionInBoundHandler = frameRecognitionInBoundHandler;
        this.parsedMessageInBoundHandler = parsedMessageInBoundHandler;
        this.recognitionOutBoundHandler = recognitionOutBoundHandler;
        this.configHandler = configHandler;
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(configHandler);
        ch.pipeline().addLast(new LoggingHandler("kyOutlineLogger", LogLevel.INFO));
        ch.pipeline().addLast(frameRecognitionInBoundHandler);
        ch.pipeline().addLast(parsedMessageInBoundHandler);
        ch.pipeline().addLast(recognitionOutBoundHandler);
    }
}
