package cc.bitky.clustermanage.db.repository;

import cc.bitky.clustermanage.db.bean.DeviceGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceGroupRepository extends MongoRepository<DeviceGroup, String> {
  DeviceGroup findByName(int name);
}
