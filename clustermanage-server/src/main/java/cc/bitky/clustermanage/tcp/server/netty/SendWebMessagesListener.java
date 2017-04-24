package cc.bitky.clustermanage.tcp.server.netty;

import java.util.List;

import cc.bitky.clustermanage.server.message.IMessage;

/**
 * Server 模块发送消息 bean 监听器
 */
@FunctionalInterface
public interface SendWebMessagesListener {

    /**
     * 向 Tcp 通道发送消息 bean 的集合
     *
     * @param iMessages 消息 bean 的集合
     * @return 是否发送成功
     */
    boolean sendMessagesToTcp(List<IMessage> iMessages);
}
