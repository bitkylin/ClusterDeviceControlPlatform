package cc.bitky.clustermanage.db.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import cc.bitky.clustermanage.db.bean.Employee;
import cc.bitky.clustermanage.db.repository.EmployeeRepository;

@Repository
public class DbEmployeePresenter {
  private final EmployeeRepository employeeRepository;
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
}
