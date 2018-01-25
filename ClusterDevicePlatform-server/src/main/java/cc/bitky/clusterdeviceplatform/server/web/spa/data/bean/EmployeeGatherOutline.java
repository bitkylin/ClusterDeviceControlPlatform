package cc.bitky.clusterdeviceplatform.server.web.spa.data.bean;

import java.util.List;

public class EmployeeGatherOutline {
    List<EmployeeGatherByDepartment> departments;
    List<EmployeeGatherByGroup> deviceGroup;

    public EmployeeGatherOutline(List<EmployeeGatherByDepartment> departments, List<EmployeeGatherByGroup> deviceGroup) {
        this.departments = departments;
        this.deviceGroup = deviceGroup;
    }

    public List<EmployeeGatherByDepartment> getDepartments() {
        return departments;
    }

    public List<EmployeeGatherByGroup> getDeviceGroup() {
        return deviceGroup;
    }
}
