package cc.bitky.clusterdeviceplatform.server.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cc.bitky.clusterdeviceplatform.server.db.bean.routineinfo.LampStatusHistory;

public interface RoutineTableRepository extends MongoRepository<LampStatusHistory, String> {
}
