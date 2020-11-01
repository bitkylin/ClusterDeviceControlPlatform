package cc.bitky.clusterdeviceplatform.server.db.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

@Getter
@Setter
@ToString
@Document(collection = "Employee")
public class Employee {

    @Id
    private String id;

    @Field("Name")
    private String name;

    @Field("Department")
    private String department;

    @Field("GroupId")
    private int groupId;

    @Field("DeviceId")
    private int deviceId;

    public Employee(String name, String department) {
        this.name = name;
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return groupId == employee.groupId &&
                deviceId == employee.deviceId &&
                Objects.equals(id, employee.id) &&
                Objects.equals(name, employee.name) &&
                Objects.equals(department, employee.department);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, department, groupId, deviceId);
    }
}
