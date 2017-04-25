package cc.bitky.clustermanage.db.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import cc.bitky.clustermanage.db.bean.KySetting;
import cc.bitky.clustermanage.db.repository.SettingRepository;
import cc.bitky.clustermanage.server.message.CardType;

@Repository
public class DbSettingPresenter {
    private final SettingRepository settingRepository;
    private final MongoOperations operations;

    @Autowired
    public DbSettingPresenter(SettingRepository settingRepository, MongoOperations operations) {
        this.settingRepository = settingRepository;
        this.operations = operations;
    }

    /**
     * 将卡号集合保存在数据库的 Setting 文档中
     *
     * @param cardArray 卡号集合
     * @param card      卡号类型枚举
     * @return 是否保存成功
     */
    boolean saveCardArray(long[] cardArray, CardType card) {
        KySetting kySetting = settingRepository.findOne("1");
        if (kySetting == null) {
            kySetting = new KySetting();
            kySetting.setId("1");
        }
        switch (card) {
            case FREE:
                kySetting.setFreeCardList(cardArray);
                break;
            case CONFIRM:
                kySetting.setConfirmCardList(cardArray);
                break;
        }
        settingRepository.save(kySetting);
        return true;
    }

    /**
     * 获取卡号的集合
     *
     * @return 卡号的集合
     */
    long[] getCardArray(CardType card) {
        KySetting kyKySettings = settingRepository.findOne("1");
        if (kyKySettings == null) return new long[0];
        long[] longs;
        switch (card) {
            case FREE:
                longs = kyKySettings.getFreeCardList();
                return longs == null ? new long[0] : longs;
            case CONFIRM:
                longs = kyKySettings.getConfirmCardList();
                return longs == null ? new long[0] : longs;
        }
        return new long[0];
    }

    /**
     * 检索数据库，给定的卡号是否匹配确认卡号
     *
     * @param cardNumber 待检索的卡号
     * @return 是否匹配确认卡号
     */
    boolean marchConfirmCard(long cardNumber) {
        Query query = new Query(Criteria.where("id").is("1").and("confirmCardList").is(cardNumber));
        return operations.exists(query, KySetting.class);
    }
}
