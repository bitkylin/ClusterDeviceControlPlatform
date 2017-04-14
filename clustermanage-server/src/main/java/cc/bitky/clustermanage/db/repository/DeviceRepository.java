package cc.bitky.clustermanage.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import cc.bitky.clustermanage.db.bean.Device;

public interface DeviceRepository extends MongoRepository<Device, String> {

    Device findFirstByGroupIdAndBoxId(int groupId, int boxId);

    List<Device> findByGroupId(int groupId);
}
