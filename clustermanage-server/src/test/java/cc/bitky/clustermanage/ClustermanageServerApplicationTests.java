package cc.bitky.clustermanage;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cc.bitky.clustermanage.db.repository.EmployeeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClustermanageServerApplicationTests {

    @Autowired
    EmployeeRepository employeeRepository;


}
