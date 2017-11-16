package cc.bitky.clusterdeviceplatform.server.db.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

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
     * 状态改变时的时间
     */
    @Field("StatusTime")
    private Date statusTime;
    /**
     * 当前状态
     */
    @Field("Status")
    private int status = 0;
    /**
     * 组号
     */
    @Field("GroupId")
    private int groupId = -1;
    /**
     * 设备号
     */
    @Field("DeviceId")
    private int deviceId = -1;

    @Field("RemainChargeTime")
    private int remainChargeTime = 500;

    public Device(int groupId, int deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmployeeObjectId() {
        return employeeObjectId;
    }

    public void setEmployeeObjectId(String employeeObjectId) {
        this.employeeObjectId = employeeObjectId;
    }

    public Date getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(Date statusTime) {
        this.statusTime = statusTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getRemainChargeTime() {
        return remainChargeTime;
    }

    public void setRemainChargeTime(int remainChargeTime) {
        this.remainChargeTime = remainChargeTime;
    }
}
