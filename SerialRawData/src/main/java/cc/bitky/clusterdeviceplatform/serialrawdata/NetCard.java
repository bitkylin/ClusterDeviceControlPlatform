package cc.bitky.clusterdeviceplatform.serialrawdata;

public class NetCard {

    private String name;
    private String mac;

    public NetCard(String name, String mac) {
        this.name = name;
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public String getMac() {
        return mac;
    }
}