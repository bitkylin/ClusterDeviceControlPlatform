package cc.bitky.clustermanage.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import cc.bitky.clustermanage.db.bean.Device;

public interface DeviceRepository extends MongoRepository<Device, String> {

    Device findFirstByGroupIdAndDeviceId(int GroupId, int DeviceId);

    List<Device> findByGroupId(int GroupId);

    Device findFirstByCardNumber(String CardNumber);
}
