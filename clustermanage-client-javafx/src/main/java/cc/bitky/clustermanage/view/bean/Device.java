package cc.bitky.clustermanage.view.bean;

import java.util.ArrayList;
import java.util.List;

public class Device {
    String name;
    String department;
    String cardNumber;
    private int groupId = -1;
    private int deviceId = -1;
    private int status = -1;
    private boolean wrong = false;
    private List<String> historyList = new ArrayList<>();

    public Device() {
    }

    public Device(int groupId, int deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
        status = 0;
    }

    public List<String> getHistoryList() {
        return historyList;
    }

    public void addHistoryList(String value) {
        this.historyList.add(value);
    }

    public boolean isWrong() {
        return wrong;
    }

    public void setWrong(boolean wrong) {
        this.wrong = wrong;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Device device = (Device) o;

        if (groupId != device.groupId) return false;
        return deviceId == device.deviceId;
    }

    @Override
    public int hashCode() {
        int result = groupId;
        result = 31 * result + deviceId;
        return result;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
}
