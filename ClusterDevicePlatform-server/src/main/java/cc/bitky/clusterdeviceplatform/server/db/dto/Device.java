package cc.bitky.clusterdeviceplatform.server.db.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "Device")
public class Device {
    /**
     * 员工卡号
     */
    @Field("CardNumber")
    private String cardNumber;

    @Id
    private String id;
    /**
     * 员工的 ObjectId
     */
    @Field("EmployeeObjectId")
    private String employeeObjectId;
    /**
     * 充电状态改变时的时间
     */
    @Field("ChargeStatusTime")
    private Date chargeStatusTime;
    /**
     * 充电状态改变时的时间
     */
    @Field("WorkStatusTime")
    private Date workStatusTime;
    /**
     * 当前状态
     */
    @Field("ChargeStatus")
    private int chargeStatus = 0;
    @Field("WorkStatus")
    private int workStatus = 0;
    /**
     * 组号
     */
    @Field("GroupId")
    private int groupId;
    /**
     * 设备号
     */
    @Field("DeviceId")
    private int deviceId;

    @Field("RemainChargeTime")
    private int remainChargeTime = 500;

    public Device(int groupId, int deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }
}
