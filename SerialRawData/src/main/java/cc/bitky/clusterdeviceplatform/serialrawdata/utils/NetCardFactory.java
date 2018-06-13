package cc.bitky.clusterdeviceplatform.serialrawdata.utils;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import cc.bitky.clusterdeviceplatform.serialrawdata.NetCard;

public class NetCardFactory {

    public static List<NetCard> createNetCards() throws SocketException {
        Enumeration<NetworkInterface> anInterface = NetworkInterface.getNetworkInterfaces();
        List<NetCard> netCards = new ArrayList<>();
        while (anInterface.hasMoreElements()) {
            NetworkInterface networkInterface = anInterface.nextElement();
            if (networkInterface.isUp() && networkInterface.getHardwareAddress() != null) {
                netCards.add(new NetCard(networkInterface.getDisplayName(), StringConvert.byteArrayToMacStr(networkInterface.getHardwareAddress())));
            }
        }
        return netCards;
    }
}
