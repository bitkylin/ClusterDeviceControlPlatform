package cc.bitky.clustermanage.db.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;

import cc.bitky.clustermanage.db.bean.Employee;
import cc.bitky.clustermanage.db.repository.EmployeeRepository;

@Repository
public class DbEmployeePresenter {
    private final EmployeeRepository employeeRepository;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Logger logger = LoggerFactory.getLogger(DbEmployeePresenter.class);

    @Autowired
    public DbEmployeePresenter(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public boolean saveEmployee(Employee employee) {

        employeeRepository.save(employee);
        Query query = new Query(Criteria.where("firstName").is("Harry"));
        return true;
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
