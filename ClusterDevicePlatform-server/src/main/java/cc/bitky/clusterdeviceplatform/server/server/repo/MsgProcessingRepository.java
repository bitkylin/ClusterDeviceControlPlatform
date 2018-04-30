package cc.bitky.clusterdeviceplatform.server.server.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;

import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyDeviceStatus;
import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.config.ServerSetting;
import cc.bitky.clusterdeviceplatform.server.db.DbPresenter;
import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.db.statistic.pressure.GroupCacheItem;
import cc.bitky.clusterdeviceplatform.server.db.statistic.pressure.MsgCount;
import cc.bitky.clusterdeviceplatform.server.db.statistic.status.DeviceGroupItem;
import cc.bitky.clusterdeviceplatform.server.db.statistic.status.DeviceGroupOutline;
import cc.bitky.clusterdeviceplatform.server.db.statistic.status.DeviceStatusItem;
import cc.bitky.clusterdeviceplatform.server.server.ServerCenterProcessor;
import cc.bitky.clusterdeviceplatform.server.server.repo.bean.StatusItem;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.channel.ChannelItem;

/**
 * 设备消息处理缓存容器
 * 用于设备向服务器大量发送状态信息的信息缓存，以便于服务器能够从容地处理捕获到的设备消息
 */
@Repository
public class MsgProcessingRepository {
    /**
     * 待处理的「状态消息」队列的容器
     */
    private final AtomicReferenceArray<LinkedBlockingDeque<MsgReplyDeviceStatus>> PROCESSING_MESSAGE_QUEUE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
    /**
     * 待处理的「状态消息」执行线程池
     */
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(DeviceSetting.MAX_GROUP_ID);
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private DbPresenter dbPresenter;
    private ServerCenterProcessor server;

    {
        //生成每个 Channel 的待发送的消息队列
        for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
            PROCESSING_MESSAGE_QUEUE.set(i, new LinkedBlockingDeque<>());

            startMessageQueueTask(PROCESSING_MESSAGE_QUEUE.get(i), i);
        }
    }

    /**
     * 「数据统计」创建单设备组服务器缓存信息bean
     *
     * @param groupId    设备组 ID
     * @param needDetail 返回值是否附带所有统计细节
     * @return 单设备组服务器缓存信息bean
     */
    private DeviceGroupItem createSingleDeviceGroup(int groupId, boolean needDetail) {
        List<DeviceStatusItem> deviceStatusItems = new ArrayList<>(DeviceSetting.MAX_DEVICE_ID);
        int deviceCount = DeviceSetting.MAX_DEVICE_ID;
        int usingCount = 0;
        int chargingCount = 0;
        int fullCount = 0;
        int uninitCount = 0;
        int msgCount = -1;
        LinkedBlockingDeque<MsgReplyDeviceStatus> deque = touchMessageQueue(groupId);
        if (deque != null) {
            msgCount = deque.size();
        }

        for (int deviceId = 1; deviceId <= DeviceSetting.MAX_DEVICE_ID; deviceId++) {
            StatusItem chargeStatus = dbPresenter.obtainStatusByCache(groupId, deviceId, MsgReplyDeviceStatus.Type.CHARGE);
            int charge = chargeStatus.getStatus();
            switch (charge) {
                case ChargeStatus.UNINIT:
                    uninitCount++;
                    break;
                case ChargeStatus.USING:
                    usingCount++;
                    break;
                case ChargeStatus.CHARGING:
                    chargingCount++;
                    break;
                case ChargeStatus.FULL:
                    fullCount++;
                    break;
                default:
                    break;
            }
            if (charge < 0) {
                charge = 0;
            }
            StatusItem workStatus = dbPresenter.obtainStatusByCache(groupId, deviceId, MsgReplyDeviceStatus.Type.WORK);

            int work = workStatus.getStatus();
            if (work < 4) {
                work = 1;
            }
            DeviceStatusItem deviceStatusItem = new DeviceStatusItem(deviceId, charge, work);
            deviceStatusItems.add(deviceStatusItem);
        }
        if (!needDetail) {
            deviceStatusItems = null;
        }
        return new DeviceGroupItem(groupId, deviceStatusItems, msgCount, deviceCount, usingCount, chargingCount, fullCount, uninitCount);
    }

    /**
     * 启动一个Channel的定时任务，用于对消息队列进行轮询，并处理指定帧
     *
     * @param deque   指定的消息发送队列
     * @param groupId 设备组 ID
     */
    private void startMessageQueueTask(LinkedBlockingDeque<MsgReplyDeviceStatus> deque, int groupId) {
        executorService.scheduleWithFixedDelay(() -> {
            try {
                MsgReplyDeviceStatus message = deque.take();
                long l1 = System.currentTimeMillis();
                Device device = dbPresenter.handleMsgDeviceStatus(message);
                //部署剩余充电次数
                if (device != null) {
                    server.deployRemainChargeTimes(device);
                }
                long l2 = System.currentTimeMillis();
                if (ServerSetting.DEBUG) {
                    logger.info("处理总时间：" + (l2 - l1) + "ms");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }, groupId, 1, TimeUnit.MILLISECONDS);
    }

    /**
     * 根据组号获取指定 channel 的消息队列
     *
     * @param groupId 设备组 ID
     */
    public LinkedBlockingDeque<MsgReplyDeviceStatus> touchMessageQueue(int groupId) {
        if (groupId > 0 && groupId <= DeviceSetting.MAX_GROUP_ID) {
            return PROCESSING_MESSAGE_QUEUE.get(groupId);
        }
        return null;
    }

    public void setDbPresenter(DbPresenter dbPresenter) {
        this.dbPresenter = dbPresenter;
    }


    public void setServer(ServerCenterProcessor server) {
        this.server = server;
    }

    //--------------- 数据统计 ---------------

    /**
     * 「数据统计」统计所有服务器缓存设备状态及待处理消息
     */
    public DeviceGroupOutline createOutline() {
        List<DeviceGroupItem> groupItems = new ArrayList<>(DeviceSetting.MAX_GROUP_ID);
        for (int groupId = 1; groupId <= DeviceSetting.MAX_GROUP_ID; groupId++) {
            groupItems.add(createSingleDeviceGroup(groupId, false));
        }
        return new DeviceGroupOutline(groupItems, DeviceSetting.MAX_GROUP_ID);
    }

    /**
     * 「数据统计」统计单个设备组缓存的设备状态及待处理消息
     *
     * @param groupId 设备组 ID
     * @return 缓存单个设备组详细信息bean
     */
    public DeviceGroupOutline createDetail(int groupId) {
        List<DeviceGroupItem> groupItems = new ArrayList<>(1);
        groupItems.add(createSingleDeviceGroup(groupId, true));
        return new DeviceGroupOutline(groupItems, DeviceSetting.MAX_GROUP_ID);
    }

    /**
     * 统计所有 Channel 的当前待发送消息数量
     */
    public GroupCacheItem statisticChannelLoad() {
        List<ChannelItem> items = new ArrayList<>(DeviceSetting.MAX_GROUP_ID);
        for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
            items.add(new ChannelItem(i, true, PROCESSING_MESSAGE_QUEUE.get(i).size()));
        }

        return new GroupCacheItem(items, new MsgCount());
    }
}
