package cc.bitky.clusterdeviceplatform.server.tcp.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceArray;

import cc.bitky.clusterdeviceplatform.messageutils.config.FrameSetting;
import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.FrameMajorHeader;
import cc.bitky.clusterdeviceplatform.messageutils.define.frame.SendableMsgContainer;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyNormal;
import cc.bitky.clusterdeviceplatform.server.config.CommSetting;
import cc.bitky.clusterdeviceplatform.server.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.server.tcp.TcpPresenter;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.channel.ChannelItem;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.channel.ChannelOutline;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.except.TcpFeedbackItem;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.HashedWheelTimer;

import static cc.bitky.clusterdeviceplatform.messageutils.config.FrameSetting.AWAKE_TO_PROCESS_INTERVAL;
import static cc.bitky.clusterdeviceplatform.server.config.CommSetting.AUTO_REPEAT_REQUEST_TIMES;
import static cc.bitky.clusterdeviceplatform.server.config.CommSetting.DEPLOY_MSG_NEED_REPLY;

@Repository
public class TcpRepository {
    /**
     * 时间轮计时器
     */
    private final HashedWheelTimer HASHED_WHEEL_TIMER = new HashedWheelTimer();
    /**
     * 已激活的 Channel 容器
     */
    private final AtomicReferenceArray<Channel> CHANNEL_ARRAY = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
    /**
     * 已接入的 Channel 容器「待识别及已激活」
     */
    private final ConcurrentHashMap<String, Integer> CHANNEL_MAP = new ConcurrentHashMap<>(DeviceSetting.MAX_GROUP_ID + 1);
    /**
     * 消息对象检错重发机制的支撑容器
     */
    private final ConcurrentHashMap<Integer, BaseMsg> RESEND_MSG_MAP = new ConcurrentHashMap<>();
    /**
     * 待发送的消息队列的容器
     */
    private final AtomicReferenceArray<LinkedBlockingDeque<BaseMsg>> SENDING_MESSAGE_QUEUE = new AtomicReferenceArray<>(DeviceSetting.MAX_GROUP_ID + 1);
    /**
     * 定时任务「定时检索待发送消息队列」执行线程池
     */
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(DeviceSetting.MAX_GROUP_ID);
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private TcpPresenter server;

    {
        //生成每个 Channel 的待发送的消息队列
        for (int i = 1; i < SENDING_MESSAGE_QUEUE.length(); i++) {
            SENDING_MESSAGE_QUEUE.set(i, new LinkedBlockingDeque<>());
            startMessageQueueTask(SENDING_MESSAGE_QUEUE.get(i), i);
        }
    }

    /**
     * 获取指定 Channel 的设备组 ID
     *
     * @param channel 指定的 Channel
     * @return 指定的设备组 ID
     */
    public int getGroupIdByChannel(Channel channel) {
        String id = channel.id().asLongText();
        return CHANNEL_MAP.getOrDefault(id, -1);
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
                Channel channel = touchChannel(channelId);
                if (channel == null || !channel.isActive()) {
                    logger.warn("「Channel」" + " Channel「" + channelId + "」无效，无法下发该帧");
                    removeChannelCompleted(channel);
                    deque.clear();
                    return;
                }
                List<ByteBuf> dataList = new ArrayList<>();
                ByteBuf data = baseMsg.subFrameEncode(channel.alloc().buffer());
                dataList.add(data);
                touchNeedReplyMsg(baseMsg);
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
                    touchNeedReplyMsg(subMsg);
                }
                FrameMajorHeader frameHeader = new FrameMajorHeader(
                        baseMsg.getMajorMsgId(),
                        baseMsg.getGroupId(),
                        baseMsg.getDeviceId(),
                        length);

                channel.writeAndFlush(new SendableMsgContainer(frameHeader, dataList));
            } catch (InterruptedException e) {
                logger.warn("消息队列定时发送任务被中断");
                //TODO 消息队列定时发送任务被中断
            }
        }, channelId, CommSetting.FRAME_SEND_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取一个已激活的 channel
     */
    public Channel touchChannel(int index) {
        if (index >= 0 && index <= DeviceSetting.MAX_GROUP_ID) {
            return CHANNEL_ARRAY.get(index);
        }
        return null;
    }

    /**
     * 对一个需要检错重发的普通消息对象，进行进一步处理「检错重发添加操作」
     *
     * @param msg 需要检错重发的普通消息对象
     */
    private void touchNeedReplyMsg(BaseMsg msg) {
        if (!DEPLOY_MSG_NEED_REPLY) {
            return;
        }
        RESEND_MSG_MAP.put(msg.getMsgFlag(), msg);
        HASHED_WHEEL_TIMER.newTimeout(task -> {
            if (RESEND_MSG_MAP.get(msg.getMsgFlag()) == null) {
                logger.info("消息已收到回复:" + msg.msgDetailToString());
                return;
            }
            if (msg.getResendTimes() < AUTO_REPEAT_REQUEST_TIMES - 1) {
                logger.warn("消息未收到回复:" + msg.msgDetailToString());
                msg.doResend();
                server.sendMessageToTcp(msg);
            } else if (server != null) {
                logger.warn("消息未收到回复已达上限:" + msg.msgDetailToString());
                server.touchUnusualMsg(TcpFeedbackItem.createResendOutBound(msg));
            }
        }, CommSetting.FRAME_SENT_TO_DETECT_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * 对一个正常回复消息对象，进行进一步处理「检错重发解除操作」
     *
     * @param msg 正常回复消息对象
     */
    public void touchNormalReplyMsg(MsgReplyNormal msg) {
        RESEND_MSG_MAP.remove(msg.getDeployMsgFlag());
    }

    /**
     * 对准备接入的 Channel 做进一步处理
     *
     * @param channel 准备接入的 Channel
     */
    public void accessibleChannel(Channel channel) {
        String id = channel.id().asLongText();
        logger.info("「Channel」" + "新的 Channel 等待接入 [" + id + "]");
        CHANNEL_MAP.put(id, -1);
        HASHED_WHEEL_TIMER.newTimeout(task -> {
            Integer index = CHANNEL_MAP.getOrDefault(id, -1);

            if (index > 0 && index <= DeviceSetting.MAX_GROUP_ID) {
                SENDING_MESSAGE_QUEUE.get(index).clear();
                Channel oldChannel = CHANNEL_ARRAY.get(index);
                if (oldChannel != null && oldChannel.isActive()) {
                    manualRemoveChannel(oldChannel);
                    manualRemoveChannel(channel);
                    logger.warn("「Channel[" + index + "]」" + "新的 Channel 欲覆盖已激活的 Channel");
                } else {
                    CHANNEL_ARRAY.set(index, channel);
                    logger.info("「Channel[" + index + "]」" + "新的 Channel 已成功装配 [" + id + "]");
                }
                return;
            }
            if (index == -1) {
                logger.warn("「Channel[" + index + "]」" + "新的 Channel 未反馈 ID [" + id + "]");
            } else {
                logger.warn("「Channel[" + index + "]」" + "新的 Channel 的 ID 超出范围 [" + id + "]");
            }
            manualRemoveChannel(channel);
        }, CommSetting.ACCESSIBLE_CHANNEL_REPLY_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * Channel 已接入成功
     *
     * @param msg     心跳包
     * @param channel 已接入成功的 Channel
     */
    public void accessChannelSuccessful(BaseMsg msg, Channel channel) {
        String id = channel.id().asLongText();
        logger.info("「Channel[" + msg.getGroupId() + "]待接入」" + "新的 Channel 接入 [" + id + "]");
        CHANNEL_MAP.put(id, msg.getGroupId());
    }

    /**
     * 手动移除相应的 Channel
     *
     * @param channel 欲被手动移除的 Channel
     */
    private void manualRemoveChannel(Channel channel) {
        if (channel.isActive()) {
            channel.disconnect();
        }
        removeChannelCompleted(channel);
    }

    /**
     * Channel 已断开，进行断开后的扫尾工作
     *
     * @param channel 已断开的 Channel
     * @return 已断开的 Channel 的 ID
     */
    public int removeChannelCompleted(Channel channel) {
        if (channel == null) {
            return -1;
        }
        String id = channel.id().asLongText();
        Integer index = CHANNEL_MAP.remove(id);
        if (index == null || index <= 0 || index > DeviceSetting.MAX_GROUP_ID) {
            logger.info("「Channel[" + (index == null ? "无" : index) + "]」" + "移除成功 Channel [" + id + "]");
            return -1;
        }
        CHANNEL_ARRAY.set(index, null);
        SENDING_MESSAGE_QUEUE.get(index).clear();
        logger.info("「Channel[" + index + "]」" + "移除成功 Channel [" + id + "]");
        return index;
    }

    /**
     * 获取指定 channel 的消息队列
     */
    public LinkedBlockingDeque<BaseMsg> touchMessageQueue(int index) {
        if (index >= 0 && index <= DeviceSetting.MAX_GROUP_ID) {
            return SENDING_MESSAGE_QUEUE.get(index);
        }
        return null;
    }

    public void setServer(TcpPresenter server) {
        this.server = server;
    }

    /**
     * 服务器优雅关闭时，关闭所有的已激活 Channel
     */
    public void removeAllChannel() {
        for (int i = 1; i < CHANNEL_ARRAY.length(); i++) {
            Channel channel = CHANNEL_ARRAY.get(i);
            if (channel != null && channel.isActive()) {
                channel.disconnect();
            }
        }
    }

    //--------------- 数据统计 ---------------

    /**
     * 统计所有 Channel 的当前待发送消息数量
     */
    public ChannelOutline statisticChannelLoad() {
        List<ChannelItem> items = new ArrayList<>(DeviceSetting.MAX_GROUP_ID);
        int activated = 0;
        int inactivated = 0;
        for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
            Channel channel = CHANNEL_ARRAY.get(i);
            boolean itemActivated = channel != null && channel.isActive();
            if (itemActivated) {
                activated++;
            } else {
                inactivated++;
            }
            ChannelItem item = new ChannelItem(i, itemActivated, SENDING_MESSAGE_QUEUE.get(i).size());
            items.add(item);
        }

        int waitToActivate = CHANNEL_MAP.size() - activated;

        return new ChannelOutline(items, activated, inactivated, waitToActivate);
    }
}
