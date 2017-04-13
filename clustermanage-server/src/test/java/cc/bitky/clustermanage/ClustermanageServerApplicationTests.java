package cc.bitky.clustermanage;

import cc.bitky.clustermanage.db.bean.Employee;
import cc.bitky.clustermanage.db.repository.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClustermanageServerApplicationTests {

  @Autowired
  EmployeeRepository employeeRepository;

  @Test
  public void contextLoads() {
    employeeRepository.save(new Employee("李明亮","总理"));
    employeeRepository.save(new Employee("吴倩倩","妻子"));
  }
}
