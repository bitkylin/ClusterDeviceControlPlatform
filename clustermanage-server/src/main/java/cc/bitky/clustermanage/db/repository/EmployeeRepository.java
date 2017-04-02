package cc.bitky.clustermanage.db.repository;

import cc.bitky.clustermanage.db.bean.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
}
