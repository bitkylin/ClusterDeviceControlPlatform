package cc.bitky.clusterdeviceplatform.server.db.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
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
}
