package cc.bitky.clusterdeviceplatform.server.db.work.bean;

import java.util.Objects;

import cc.bitky.clusterdeviceplatform.server.db.bean.Employee;
import cc.bitky.clusterdeviceplatform.server.server.utils.DeviceOutBoundDetect;

public class EmployeeItem {
    //个人信息
    private String department;

    //设备位置
    private int groupId;

    private int deviceId;

    private EmployeeItem(Employee employee) {
        this.department = employee.getDepartment();
        this.groupId = employee.getGroupId();
        this.deviceId = employee.getDeviceId();
    }

    public static EmployeeItem create(Employee employee) {
        if (DeviceOutBoundDetect.detect(employee.getGroupId(), employee.getDeviceId())) {
            return null;
        }
        return new EmployeeItem(employee);
    }

    public String getDepartment() {
        return department;
    }

    public int getGroupId() {
        return groupId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EmployeeItem that = (EmployeeItem) o;
        return groupId == that.groupId &&
                deviceId == that.deviceId &&
                Objects.equals(department, that.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(department, groupId, deviceId);
    }
}
