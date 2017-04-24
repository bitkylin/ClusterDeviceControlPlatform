package cc.bitky.clustermanage.db.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cc.bitky.clustermanage.db.bean.KySetting;
import cc.bitky.clustermanage.db.repository.SettingRepository;
import cc.bitky.clustermanage.server.bean.ServerWebMessageHandler.Card;

@Repository
public class DbSettingPresenter {
    private final SettingRepository settingRepository;

    @Autowired
    public DbSettingPresenter(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    /**
     * 将卡号集合保存在数据库的 Setting 文档中
     *
     * @param cardArray 卡号集合
     * @param card      卡号类型枚举
     * @return 是否保存成功
     */
    boolean saveCardArray(long[] cardArray, Card card) {
        KySetting kySetting = settingRepository.findOne("1");
        if (kySetting == null) {
            kySetting = new KySetting();
            kySetting.setId(1);
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
    long[] getCardArray(Card card) {
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
}
