package cc.bitky.clusterdeviceplatform.server.tcp.exception;

/**
 * 触发异常消息的回调接口
 */
public interface MsgTouch {

    void touchUnusualMsg(ExceptionMsgTcp msg);
}
