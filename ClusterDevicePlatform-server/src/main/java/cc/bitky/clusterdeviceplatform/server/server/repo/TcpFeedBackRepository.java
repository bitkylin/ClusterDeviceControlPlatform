package cc.bitky.clusterdeviceplatform.server.server.repo;

import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import cc.bitky.clusterdeviceplatform.server.config.DbSetting;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.except.TcpFeedbackItem;

/**
 * 用于TCP反馈消息对象的缓存容器
 */
@Repository
public class TcpFeedBackRepository {
    public static final AtomicInteger FEEDBACK_ITEMS_COUNT = new AtomicInteger();
    private final LinkedBlockingDeque<TcpFeedbackItem> feedbackItems = new LinkedBlockingDeque<>();
    private final ConcurrentHashMap<String, Instant> userSet = new ConcurrentHashMap<>();

    /**
     * 向队列中添加该项，若已存在该项，则忽略
     *
     * @param item 欲添加的项目
     */
    public void putItemIfAbsent(TcpFeedbackItem item) {
        if (!feedbackItems.contains(item)) {
            feedbackItems.offer(item);
        }
        if (feedbackItems.size() > DbSetting.FEEDBACK_ITEM_SIZE_MAX) {
            feedbackItems.poll();
        }
        FEEDBACK_ITEMS_COUNT.set(feedbackItems.size());
    }

//    /**
//     * 向队列中添加该项，若已存在该项，则覆盖
//     *
//     * @param item 欲添加的项目
//     */
//    public void putItem(TcpFeedbackItem item) {
//        feedbackItems.remove(item);
//        feedbackItems.offer(item);
//        if (feedbackItems.size() > DbSetting.FEEDBACK_ITEM_SIZE_MAX) {
//            feedbackItems.poll();
//        }
//    }

    public List<TcpFeedbackItem> getItems(String userId) {
        Instant instant = userSet.getOrDefault(userId, Instant.MIN);
        userSet.put(userId, Instant.now());
        Iterator<TcpFeedbackItem> iterator = feedbackItems.iterator();
        List<TcpFeedbackItem> items = new ArrayList<>(DbSetting.FEEDBACK_ITEM_SIZE_MAX);
        iterator.forEachRemaining(item -> {
            if (instant.isBefore(item.getInstant())) {
                items.add(item);
            }
        });
        return items;
    }

    public void clearItems() {
        feedbackItems.clear();
        FEEDBACK_ITEMS_COUNT.set(0);
    }
}
