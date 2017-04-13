package cc.bitky.clustermanage.db.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Employee {
  @Id
  String id;
  //个人信息
  String name;
  String department;
  long cardNumber;
  //int age;
  //设备位置
  int groupId;
  int deviceId;
  String routineInfoObjectId;
  public Employee() {
  }
  public Employee(String name, String department) {
    this.name = name;
    this.department = department;
    //age = 18;
  }

  public long getCardNumber() {
    return cardNumber;
  }

  public void setCardNumber(long cardNumber) {
    this.cardNumber = cardNumber;
  }

  @Override public String toString() {
    return "Employee{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", department='" + department + '\'' +
        ", groupId=" + groupId +
        ", deviceId=" + deviceId +
        ", routineInfoObjectId='" + routineInfoObjectId + '\'' +
        '}';
  }

  //public int getAge() {
  //  return age;
  //}
  //
  //public void setAge(int age) {
  //  this.age = age;
  //}

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

  public String getRoutineInfoObjectId() {
    return routineInfoObjectId;
  }

  public void setRoutineInfoObjectId(String routineInfoObjectId) {
    this.routineInfoObjectId = routineInfoObjectId;
  }
}
