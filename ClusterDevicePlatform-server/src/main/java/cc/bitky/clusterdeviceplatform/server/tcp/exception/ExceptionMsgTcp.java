package cc.bitky.clusterdeviceplatform.server.tcp.exception;

import cc.bitky.clusterdeviceplatform.messageutils.define.BaseMsg;

public class ExceptionMsgTcp {

    private final BaseMsg baseMsg;
    private final Type type;

    public ExceptionMsgTcp(BaseMsg baseMsg, Type type) {
        this.baseMsg = baseMsg;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public BaseMsg getBaseMsg() {
        return baseMsg;
    }

    /**
     * 获取异常消息的描述信息
     *
     * @return 异常消息的描述信息
     */
    public String getDetail() {
        return type.getDetail();
    }

    /**
     * 异常消息类型
     */
    public enum Type {
        /**
         * 检错重发超出最大次数
         */
        RESEND_OUT_BOUND("消息重发次数超出限制");

        private String detail;

        Type(String detail) {
            this.detail = detail;
        }

        String getDetail() {
            return detail;
        }
    }
}
