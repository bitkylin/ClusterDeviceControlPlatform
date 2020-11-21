package cc.bitky.clusterdeviceplatform.server.db.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import cc.bitky.clusterdeviceplatform.server.db.dto.Employee;


public interface EmployeeRepository extends MongoRepository<Employee, String> {
}
