package cc.bitky.clustermanage.tcp.server.netty;

import cc.bitky.clustermanage.server.message.base.IMessage;

/**
 * Server 模块发送消息 bean 监听器
 */
@FunctionalInterface
public interface SendWebMessagesListener {

    /**
     * 向 Tcp 通道发送消息 bean 的集合
     *
     * @param message 消息 bean 的集合
     * @return 是否发送成功
     */
    boolean sendMessagesToTcp(IMessage message);
}
