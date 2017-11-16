package cc.bitky.clusterdeviceplatform.server.db.operate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import cc.bitky.clusterdeviceplatform.server.db.bean.Employee;
import cc.bitky.clusterdeviceplatform.server.db.repository.EmployeeRepository;

@Repository
public class EmployeeOperate {

    private final EmployeeRepository repository;

    @Autowired
    public EmployeeOperate(EmployeeRepository repository) {
        this.repository = repository;
    }

    public Optional<Employee> queryEmployee(String objectId) {
        return repository.findById(objectId);
    }
}
