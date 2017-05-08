package cc.bitky.clustermanage.db.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection = "Dev")
public class Device {
    /**
     * 员工卡号
     */
    @Field("cn")
    long cardNumber;

    @Id
    private String id;
    /**
     * 员工的 ObjectId
     */
    @Field("eoid")
    private String employeeObjectId;
    /**
     * 状态改变时的时间
     */
    @Field("tie")
    private Date time;
    /**
     * 当前状态
     */
    @Field("sta")
    private int status = 0;
    /**
     * 组号
     */
    @Field("gId")
    private int groupId = -1;
    /**
     * 设备号
     */
    @Field("bId")
    private int boxId = -1;

    @Field("reTime")
    private int remainChargeTime = 500;

    public Device(int groupId, int boxId) {
        this.groupId = groupId;
        this.boxId = boxId;
    }

    public Device() {
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public int getBoxId() {
        return boxId;
    }

    public void setBoxId(int boxId) {
        this.boxId = boxId;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getRemainChargeTime() {
        return remainChargeTime;
    }

    public void setRemainChargeTime(int remainChargeTime) {
        this.remainChargeTime = remainChargeTime;
    }
}
