package cc.bitky.clustermanage.db.presenter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;

import cc.bitky.clustermanage.db.bean.Employee;
import cc.bitky.clustermanage.db.repository.EmployeeRepository;

@Repository
public class DbEmployeePresenter {
    private final EmployeeRepository employeeRepository;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public DbEmployeePresenter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    Employee ObtainEmployeeByObjectId(String employeeObjectId) {
        if (employeeObjectId == null) return null;
        return employeeRepository.findOne(employeeObjectId);
    }

    Employee createEmployee(int groupId, int boxId) {
        Employee employee = new Employee("备用「" + simpleDateFormat.format(new Date()) + "」", "");
        employee.setGroupId(groupId);
        employee.setDeviceId(boxId);
        employee = employeeRepository.save(employee);
        return employee;
    }

}
