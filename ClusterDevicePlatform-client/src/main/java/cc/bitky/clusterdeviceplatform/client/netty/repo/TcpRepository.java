package cc.bitky.clusterdeviceplatform.client.netty.repo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;

import cc.bitky.clusterdeviceplatform.client.config.CommSetting;
import cc.bitky.clusterdeviceplatform.client.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.messageutils.config.FrameSetting;
import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.FrameMajorHeader;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.SendableMsgContainer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.HashedWheelTimer;

import static cc.bitky.clusterdeviceplatform.messageutils.config.FrameSetting.AWAKE_TO_PROCESS_INTERVAL;

@Repository
public class TcpRepository {
    /**
     * 时间轮计时器
     */
    private final HashedWheelTimer HASHED_WHEEL_TIMER = new HashedWheelTimer();
    /**
     * 已激活「识别」的 Channel 容器
     */
    private final AtomicReferenceArray<Channel> CHANNEL_ARRAY = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
    /**
     * 待发送的消息队列的容器
     */
    private final AtomicReferenceArray<LinkedBlockingDeque<BaseMsg>> SENDING_MESSAGE_QUEUE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
    /**
     * 定时任务「定时检索待发送消息队列」执行线程池
     */
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(DeviceSetting.MAX_GROUP_ID);
    private final Logger logger = LoggerFactory.getLogger(TcpRepository.class);

    {
        //生成每个 Channel 的待发送的消息队列
        for (int i = 1; i < SENDING_MESSAGE_QUEUE.length(); i++) {
            SENDING_MESSAGE_QUEUE.set(i, new LinkedBlockingDeque<>());
            startMessageQueueTask(SENDING_MESSAGE_QUEUE.get(i), i);
        }
    }


    /**
     * 获取特定的已激活的 channel
     *
     * @param index 待获取的 channel 的序号
     * @return 已获取的 channel
     */
    public Optional<Channel> touchChannel(int index) {
        if (index >= 0 && index <= DeviceSetting.MAX_GROUP_ID) {
            return Optional.ofNullable(CHANNEL_ARRAY.get(index));
        } else {
            return Optional.empty();
        }
    }

    /**
     * 启动一个Channel的定时任务，用于间隔指定的时间对消息队列进行轮询，并发送指定帧
     *
     * @param deque     指定的消息发送队列
     * @param channelId 指定 Channel 的序号
     */
    private void startMessageQueueTask(LinkedBlockingDeque<BaseMsg> deque, Integer channelId) {
        executorService.scheduleWithFixedDelay(() -> {
            try {
                BaseMsg baseMsg = deque.take();
                Thread.sleep(AWAKE_TO_PROCESS_INTERVAL);
                touchChannel(channelId).ifPresent(channel -> {
                    if (!channel.isActive()) {
                        deque.clear();
                        return;
                    }
                    List<ByteBuf> dataList = new ArrayList<>();
                    ByteBuf data = baseMsg.subFrameEncode(channel.alloc().buffer());
                    dataList.add(data);
                    int length = data.readableBytes();
                    int flag = baseMsg.combineFrameFlag();
                    while (true) {
                        BaseMsg subMsg = deque.peek();
                        if (subMsg == null || subMsg.combineFrameFlag() != flag) {
                            break;
                        }
                        data = subMsg.subFrameEncode(channel.alloc().buffer());
                        if (length + data.readableBytes() > FrameSetting.MAX_DATA_LENGTH) {
                            break;
                        }
                        length += data.readableBytes();
                        dataList.add(data);
                        deque.poll();
                    }
                    FrameMajorHeader frameHeader = new FrameMajorHeader(
                            baseMsg.getMajorMsgId(),
                            baseMsg.getGroupId(),
                            baseMsg.getDeviceId(),
                            length);
                    channel.writeAndFlush(new SendableMsgContainer(frameHeader, dataList));
                });
            } catch (InterruptedException e) {
                logger.warn("消息队列定时发送任务被中断");
                //TODO 消息队列定时发送任务被中断
            }
        }, 0, CommSetting.FRAME_SEND_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * 保存已激活的 Channel
     *
     * @param channel 已激活的 Channel
     */
    public void activeChannel(Channel channel) {
        Attribute<Integer> key = channel.attr(AttributeKey.valueOf("ID"));
        int id = key.get();

        CHANNEL_ARRAY.set(id, channel);
        SENDING_MESSAGE_QUEUE.get(id).clear();
    }

    /**
     * 清除已失效的 Channel
     *
     * @param i 已失效的 Channel 的序号
     */
    public void inactiveChannel(int i) {
        CHANNEL_ARRAY.set(i, null);
    }

    /**
     * 获取指定 channel 的消息队列
     */
    public Optional<LinkedBlockingDeque<BaseMsg>> touchMessageQueue(int index) {
        if (index >= 0 && index <= DeviceSetting.MAX_GROUP_ID) {
            return Optional.ofNullable(SENDING_MESSAGE_QUEUE.get(index));
        } else {
            return Optional.empty();
        }
    }
}
