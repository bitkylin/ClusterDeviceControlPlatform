package cc.bitky.clustermanage.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cc.bitky.clustermanage.db.bean.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
}
