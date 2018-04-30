package cc.bitky.clusterdeviceplatform.server.server.repo.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.msg.statusreply.MsgReplyNormal;
import cc.bitky.clusterdeviceplatform.server.db.bean.Employee;

public class DeviceItem {

    private final AtomicReference<Employee> employee = new AtomicReference<>();
    private final AtomicReference<StatusItem> chargeStatus = new AtomicReference<>(new StatusItem());
    private final AtomicReference<StatusItem> workStatus = new AtomicReference<>(new StatusItem());
    private final Map<Integer, BaseMsg> cacheMsg = new HashMap<>(64);
    private final int groupId;
    private final int deviceId;
    private int msgCount = 0;

    public DeviceItem(int groupId, int deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public StatusItem obtainChargeStatus() {
        return chargeStatus.get();
    }

    public void saveChargeStatus(StatusItem status) {
        chargeStatus.set(status);
    }

    public StatusItem obtainWorkStatus() {
        return workStatus.get();
    }

    public void saveWorkStatus(StatusItem status) {
        workStatus.set(status);
    }

    public Optional<Employee> obtainEmployee() {
        return Optional.ofNullable(employee.get());
    }

    public void saveEmployee(Employee employee) {
        this.employee.set(employee);
    }

    public void saveMsg(BaseMsg msg) {
        cacheMsg.put(msg.getJointMsgFlag(), msg);
        if (msgCount < cacheMsg.size()) {
            msgCount = cacheMsg.size();
        }
    }

    public void removeMsg(MsgReplyNormal msg) {
        cacheMsg.remove(msg.getDeployJointMsgFlag());
        if (cacheMsg.isEmpty()) {
            msgCount = 0;
        }
    }

    public Collection<BaseMsg> getCacheMsg() {
        return cacheMsg.values();
    }

    public int getMsgCount() {
        return msgCount;
    }

    public int getMsgSendingCount() {
        return cacheMsg.size();
    }
}
