package cc.bitky.clustermanage.db.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Employee {
  @Id
  String id;
  String name;
  String age;
  String routineInfoObjectId;

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

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  public String getRoutineInfoObjectId() {
    return routineInfoObjectId;
  }

  public void setRoutineInfoObjectId(String routineInfoObjectId) {
    this.routineInfoObjectId = routineInfoObjectId;
  }
}
