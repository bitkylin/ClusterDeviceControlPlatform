package cc.bitky.clusterdeviceplatform.server.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import cc.bitky.clusterdeviceplatform.server.db.bean.Device;
import cc.bitky.clusterdeviceplatform.server.db.bean.Employee;
import cc.bitky.clusterdeviceplatform.server.db.operate.CardSetOperate;
import cc.bitky.clusterdeviceplatform.server.db.operate.DeviceOperate;
import cc.bitky.clusterdeviceplatform.server.db.operate.EmployeeOperate;
import cc.bitky.clusterdeviceplatform.server.web.bean.CardType;
import reactor.core.publisher.Mono;

@Service
public class DbPresenter {

    private final CardSetOperate cardSetOperate;
    private final DeviceOperate deviceOperate;
    private final EmployeeOperate employeeOperate;


    @Autowired
    public DbPresenter(CardSetOperate cardSetOperate, DeviceOperate deviceOperate, EmployeeOperate employeeOperate) {
        this.cardSetOperate = cardSetOperate;
        this.deviceOperate = deviceOperate;
        this.employeeOperate = employeeOperate;
    }

    /**
     * 保存特定的卡号组
     *
     * @param cards 特定的卡号组
     * @param type         卡号组的类型
     * @return 保存成功
     */
    public boolean saveCardSet(String[] cards, CardType type) {
        cardSetOperate.saveCardSet(type.name(), cards);
        return true;
    }

    /**
     * 根据类型获取指定的卡号组
     *
     * @param type 卡号组的类型
     * @return 特定的卡号组
     */
    public Mono<String[]> queryCardSet(CardType type) {
        return Mono.just(cardSetOperate.obtainCardSet(type.name()).block().getCardList());

    }

    /**
     * 获取设备的集合
     *
     * @param groupId  设备组 Id
     * @param deviceId 设备 Id
     * @return 设备的集合
     */
    public List<Device> queryDeviceInfo(int groupId, int deviceId) {
        return deviceOperate.queryDeviceInfo(groupId, deviceId);
    }

    /**
     * 保存特定的设备
     *
     * @param device 特定的设备
     * @return 特定的设备
     */
    public Device saveDeviceInfo(Device device) {
        return deviceOperate.saveDeviceInfo(device);
    }

    public Optional<Employee> queryEmployee(String objectId) {
        return employeeOperate.queryEmployee(objectId);
    }
}
