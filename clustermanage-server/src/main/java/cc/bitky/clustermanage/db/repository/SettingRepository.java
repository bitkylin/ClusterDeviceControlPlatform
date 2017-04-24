package cc.bitky.clustermanage.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cc.bitky.clustermanage.db.bean.KySetting;

public interface SettingRepository extends MongoRepository<KySetting, String> {

}
