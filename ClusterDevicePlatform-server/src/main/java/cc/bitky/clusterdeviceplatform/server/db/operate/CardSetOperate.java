package cc.bitky.clusterdeviceplatform.server.db.operate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cc.bitky.clusterdeviceplatform.server.db.dto.CardSet;
import cc.bitky.clusterdeviceplatform.server.db.repository.CardSetRepository;
import reactor.core.publisher.Mono;

@Repository
public class CardSetOperate {

    private final CardSetRepository repository;

    @Autowired
    public CardSetOperate(CardSetRepository repository) {
        this.repository = repository;
    }

    /**
     * 保存特定的卡号组
     *
     * @param objectId 卡号组的名称
     * @param cardSet  特定的卡号组
     * @return 已保存的卡号组
     */
    public Mono<CardSet> saveCardSet(String objectId, String[] cardSet) {
        return repository.save(new CardSet(objectId, cardSet));
    }

    /**
     * 根据名称获取指定的卡号组
     *
     * @param name 卡号组的名称
     * @return 特定的卡号组
     */
    public Mono<CardSet> obtainCardSet(String name) {
        return repository.findById(name);
    }
}
