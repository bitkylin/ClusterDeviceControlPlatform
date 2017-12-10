package cc.bitky.clusterdeviceplatform.client.ui.bean;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import cc.bitky.clusterdeviceplatform.client.ui.view.DeviceCellView;
import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.config.WorkStatus;
import cc.bitky.clusterdeviceplatform.messageutils.define.base.BaseMsg;
import cc.bitky.clusterdeviceplatform.messageutils.msg.MsgCardSet;
import cc.bitky.clusterdeviceplatform.messageutils.msg.device.MsgEmployeeDepartment;
import cc.bitky.clusterdeviceplatform.messageutils.msg.device.MsgEmployeeName;

public class Device {
    private String name;
    private String department;
    private String cardNumber;
    private AtomicReference<DeviceCellView> viewGetter = new AtomicReference<>();
    private int groupId;
    private int deviceId;
    private int chargeStatus;
    private int workStatus;
    private List<String> historyList = new ArrayList<>();

    Device(int groupId, int deviceId) {
        this.groupId = groupId;
        this.deviceId = deviceId;
        chargeStatus = ChargeStatus.UNINIT;
        workStatus = WorkStatus.NORMAL;
    }

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public void handleMsg(BaseMsg msg) {
        historyList.add(msg.getMsgDetail());
        switch (msg.getMajorMsgId()) {
            case 0x10:
                updateDeviceView(msg);
                break;
            default:
        }
    }

    private void updateDeviceView(BaseMsg msg) {
        switch (msg.getSubMsgId()) {
            case 0x1C:
                MsgEmployeeName msgEmployeeName = (MsgEmployeeName) msg;
                name = msgEmployeeName.getName();
                break;
            case 0x1D:
                MsgEmployeeDepartment msgEmployeeDepartment = (MsgEmployeeDepartment) msg;
                department = msgEmployeeDepartment.getDepartment();
                break;
            case 0x1E:
                MsgCardSet cardEmployee = (MsgCardSet) msg;
                cardNumber = cardEmployee.getCardStr();
                break;
            default:
        }
        DeviceCellView deviceCellView = viewGetter.get();
        if (deviceCellView == null) {
            return;
        }
        deviceCellView.refreshCell(this);
    }

    /**
     * 移除钩子
     */
    public void removeHook() {
        viewGetter.set(null);
    }

    /**
     * 绑定新的钩子
     *
     * @param deviceCellView 钩住设备显示窗格
     */
    public void bindHook(DeviceCellView deviceCellView) {
        viewGetter.set(deviceCellView);
    }

    public List<String> getHistoryList() {
        return historyList;
    }

    public void addHistoryList(String value) {
        this.historyList.add(value);
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

    public int getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(int chargeStatus) {
        this.chargeStatus = chargeStatus;
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

    public DeviceCellView getView() {
        return viewGetter.get();
    }

    public void setView(DeviceCellView cell) {
        viewGetter.set(cell);
    }

}
