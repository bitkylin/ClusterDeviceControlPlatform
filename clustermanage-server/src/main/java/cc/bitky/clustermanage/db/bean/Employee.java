package cc.bitky.clustermanage.db.bean;

import org.springframework.data.annotation.Id;

//@Document(collection = "Emp")
public class Employee {

    @Id
    String id;

    //个人信息
  //  @Field("na")
    String name;

  //  @Field("dep")
    String department;

    //设备位置
 //   @Field("gId")
    int groupId;

 //   @Field("dId")
    int deviceId;

    public Employee(String name, String department) {
        this.name = name;
        this.department = department;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", groupId=" + groupId +
                ", deviceId=" + deviceId +
                '}';
    }
}
