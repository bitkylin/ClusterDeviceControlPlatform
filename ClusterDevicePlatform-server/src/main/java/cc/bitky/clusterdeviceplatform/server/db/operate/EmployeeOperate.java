package cc.bitky.clusterdeviceplatform.server.db.operate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    /**
     * 根据 ObjectId 检索指定的员工对象
     *
     * @param objectId 员工的 ObjectId
     * @return 指定的员工对象
     */
    public Optional<Employee> queryEmployee(String objectId) {
        return repository.findById(objectId);
    }

    /**
     * 检索所有的员工对象
     *
     * @return 员工对象集合
     */
    public List<Employee> queryEmployeeAll() {
        return repository.findAll();
    }
}
