package cc.bitky.clusterdeviceplatform.server.web.spa.utils;

public class ResMsg {

    int code = 20000;

    Object data = null;

    public ResMsg(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }
}
