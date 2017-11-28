package cc.bitky.clusterdeviceplatform.server.db.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "DeviceGroup")
public class DeviceGroup {

    @Id
    private String id;

    @Field("HeartBeatTime")
    private Date heartBeatTime;

    @Field("GroupId")
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
