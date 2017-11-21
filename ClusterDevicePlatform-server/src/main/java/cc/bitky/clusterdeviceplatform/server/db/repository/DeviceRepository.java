package cc.bitky.clusterdeviceplatform.server.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

import cc.bitky.clusterdeviceplatform.server.db.bean.Device;


public interface DeviceRepository extends MongoRepository<Device, String> {

    /**
     * 通过组号和设备号查询特定设备
     *
     * @param groupId  组号
     * @param deviceId 设备号
     * @return 查询到的特定设备
     */
    List<Device> findByGroupIdAndDeviceId(int groupId, int deviceId);

    Device findFirstByGroupIdAndDeviceId(int groupId, int deviceId);

    /**
     * 通过组号查询特定设备组
     *
     * @param groupid 组号
     * @return 查询到的设备组
     */
    List<Device> findByGroupId(int groupid);
}
