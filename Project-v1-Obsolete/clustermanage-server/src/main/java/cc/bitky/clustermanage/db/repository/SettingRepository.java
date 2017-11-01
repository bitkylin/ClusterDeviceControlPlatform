package cc.bitky.clustermanage.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cc.bitky.clustermanage.db.bean.Cards;

public interface SettingRepository extends MongoRepository<Cards, String> {

}
