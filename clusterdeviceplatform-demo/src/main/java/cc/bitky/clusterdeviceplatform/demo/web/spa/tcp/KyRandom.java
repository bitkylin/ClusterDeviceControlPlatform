package cc.bitky.clusterdeviceplatform.demo.web.spa.tcp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cc.bitky.clusterdeviceplatform.demo.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.demo.tcp.statistic.channel.ChannelItem;
import cc.bitky.clusterdeviceplatform.demo.tcp.statistic.channel.ChannelOutline;

public class KyRandom {
    public ChannelOutline create() {
        Random random = new Random();
        int size = DeviceSetting.MAX_GROUP_ID;
        List<ChannelItem> items = new ArrayList<>(size);
        int activatedCount = 0;
        int inactivatedCount = 0;
        int waitToActivateCount = random.nextInt(20);

        for (int i = 1; i <= size; i++) {
            boolean enabled = random.nextBoolean();
            if (enabled) {
                activatedCount++;
            } else {
                inactivatedCount++;
            }
            ChannelItem item = new ChannelItem(i, enabled, random.nextInt(600));
            items.add(item);
        }
        return new ChannelOutline(items, activatedCount, inactivatedCount, waitToActivateCount);
    }
}
