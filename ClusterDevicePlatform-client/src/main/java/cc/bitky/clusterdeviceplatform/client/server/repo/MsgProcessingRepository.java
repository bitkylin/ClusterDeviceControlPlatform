package cc.bitky.clusterdeviceplatform.client.server.repo;

import org.springframework.stereotype.Service;

@Service
public class MsgProcessingRepository {
//    /**
//     * 待处理的「状态消息」队列的容器
//     */
//    private final AtomicReferenceArray<LinkedBlockingDeque<MsgPackage>> PROCESSING_MESSAGE_QUEUE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
//    /**
//     * 待处理的「状态消息」执行线程池
//     */
//    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(DeviceSetting.MAX_GROUP_ID);
//    private final Logger logger = LoggerFactory.getLogger(getClass());
//    private ServerWebHandler server;
//
//    {
//        //生成每个 Channel 的待发送的消息队列
//        for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
//            PROCESSING_MESSAGE_QUEUE.set(i, new LinkedBlockingDeque<>());
//            startMessageQueueTask(PROCESSING_MESSAGE_QUEUE.get(i), i);
//        }
//    }
//    /**
//     * 根据组号获取指定 channel 的消息队列
//     *
//     * @param groupId 设备组 ID
//     */
//    public LinkedBlockingDeque<MsgPackage> touchMessageQueue(int groupId) {
//        if (groupId > 0 && groupId <= DeviceSetting.MAX_GROUP_ID) {
//            return PROCESSING_MESSAGE_QUEUE.get(groupId);
//        }
//        return null;
//    }
//    /**
//     * 启动一个Channel的定时任务，用于对消息队列进行轮询，并处理指定帧
//     *
//     * @param deque   指定的消息发送队列
//     * @param groupId 设备组 ID
//     */
//    private void startMessageQueueTask(LinkedBlockingDeque<MsgPackage> deque, int groupId) {
//        executorService.scheduleWithFixedDelay(() -> {
//            try {
//
//                MsgPackage message = deque.take();
//                server.getTcp().sendMessage();
//                //部署剩余充电次数
//                if (device != null) {
//                    server.deployRemainChargeTimes(device);
//                }
//                long l2 = System.currentTimeMillis();
//                if (ServerSetting.DEBUG) {
//                    logger.info("处理总时间：" + (l2 - l1) + "ms");
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//        }, groupId, 1000, TimeUnit.MILLISECONDS);
//    }
//
//    public void setServer(ServerWebHandler server) {
//        this.server = server;
//    }
}
