package cc.bitky.clustermanage.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cc.bitky.clustermanage.db.bean.DeviceGroup;

public interface DeviceGroupRepository extends MongoRepository<DeviceGroup, String> {
    DeviceGroup findByGroupId(int groupId);
}
