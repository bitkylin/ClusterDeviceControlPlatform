package cc.bitky.clusterdeviceplatform.server.server.repo;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import cc.bitky.clusterdeviceplatform.server.config.DbSetting;
import cc.bitky.clusterdeviceplatform.server.tcp.statistic.except.TcpFeedbackItem;

/**
 * 用于TCP反馈消息对象的缓存容器
 */
@Repository
public class TcpFeedBackRepository {
    private final LinkedBlockingDeque<TcpFeedbackItem> feedbackItems = new LinkedBlockingDeque<>();

    /**
     * 向队列中添加该项，若已存在该项，则覆盖
     *
     * @param item 欲添加的项目
     */
    public void putItem(TcpFeedbackItem item) {
        feedbackItems.remove(item);
        feedbackItems.offer(item);
        if (feedbackItems.size() > DbSetting.FEEDBACK_ITEM_SIZE_MAX) {
            feedbackItems.poll();
        }
    }

    public void removeItem(TcpFeedbackItem item) {
        feedbackItems.remove(item);
    }

    public List<TcpFeedbackItem> getItems() {
        Iterator<TcpFeedbackItem> iterator = feedbackItems.iterator();
        List<TcpFeedbackItem> items = new ArrayList<>(DbSetting.FEEDBACK_ITEM_SIZE_MAX);
        iterator.forEachRemaining(items::add);
        return items;
    }

    public void clearItems() {
        feedbackItems.clear();
    }
}
