package cc.bitky.clustermanage.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cc.bitky.clustermanage.db.bean.routineinfo.RoutineTables;

public interface RoutimeTableRepository extends MongoRepository<RoutineTables, String> {

}
