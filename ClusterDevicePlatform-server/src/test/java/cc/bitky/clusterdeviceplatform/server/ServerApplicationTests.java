package cc.bitky.clusterdeviceplatform.server;

import java.util.concurrent.atomic.AtomicReference;

import cc.bitky.clusterdeviceplatform.server.db.dto.Employee;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class ServerApplicationTests {
    //    @Test
    public void contextLoads() {
        int[][] d = new int[5][];
        AtomicReference<Employee> employee = new AtomicReference<>();
        Employee employee1 = employee.get();
        System.out.println();
    }
}


