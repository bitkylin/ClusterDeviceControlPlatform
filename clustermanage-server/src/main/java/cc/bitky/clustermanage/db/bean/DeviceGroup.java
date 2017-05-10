package cc.bitky.clustermanage.db.bean;

import org.springframework.data.annotation.Id;

import java.util.Date;

//@Document(collection = "DevGroup")
public class DeviceGroup {

    @Id
    private String id;

 //   @Field("hbt")
    private Date heartBeatTime;

 //   @Field("gId")
    private int groupId;


    public DeviceGroup(int groupId) {
        this.groupId = groupId;
        heartBeatTime = new Date(0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getHeartBeatTime() {
        return heartBeatTime;
    }

    public void setHeartBeatTime(Date heartBeatTime) {
        this.heartBeatTime = heartBeatTime;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
