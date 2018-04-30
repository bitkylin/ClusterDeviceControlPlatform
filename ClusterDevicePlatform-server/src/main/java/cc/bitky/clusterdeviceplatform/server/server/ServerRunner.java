package cc.bitky.clusterdeviceplatform.server.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;

import cc.bitky.clusterdeviceplatform.server.db.bean.Employee;
import cc.bitky.clusterdeviceplatform.server.server.repo.DeviceStatusRepository;
import cc.bitky.clusterdeviceplatform.server.server.utils.DeviceOutBoundDetect;

@Service
public class ServerRunner implements CommandLineRunner {

    @Autowired
    private final ServerCenterProcessor centerProcessor;

    public ServerRunner(ServerCenterProcessor centerProcessor) {
        this.centerProcessor = centerProcessor;
    }

    public ServerCenterProcessor getCenterProcessor() {
        return centerProcessor;
    }

    public boolean rebuildEmployeeStatus() {
        DeviceStatusRepository repository = centerProcessor.getDeviceStatusRepository();
        repository.clearEmployeeItemsRef();
        List<Employee> employeeList = centerProcessor.getDbPresenter().getEmployeeOperate().queryEmployeeAll();

        employeeList.forEach(employee -> {
            if (DeviceOutBoundDetect.detect(employee.getGroupId(), employee.getDeviceId())) {
                return;
            }
            repository.addEmployeeItemsRef(employee.getGroupId(), employee.getDeviceId(), employee);
        });
        return true;
    }

    @Override
    public void run(String... args) throws Exception {
        Executors.newSingleThreadExecutor().submit(this::rebuildEmployeeStatus);
    }
}
