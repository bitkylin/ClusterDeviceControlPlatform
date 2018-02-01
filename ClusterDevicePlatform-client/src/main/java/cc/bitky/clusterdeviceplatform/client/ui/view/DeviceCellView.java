package cc.bitky.clusterdeviceplatform.client.ui.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTabPane;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import cc.bitky.clusterdeviceplatform.client.ui.UiPresenter;
import cc.bitky.clusterdeviceplatform.client.ui.bean.Device;
import cc.bitky.clusterdeviceplatform.messageutils.config.ChargeStatus;
import cc.bitky.clusterdeviceplatform.messageutils.config.WorkStatus;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.statusreply.MsgCodecReplyStatusCharge;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.statusreply.MsgCodecReplyStatusWork;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;

public class DeviceCellView extends JFXTabPane {

    private AtomicReference<Device> hookedDeviceGetter = new AtomicReference<>();
    @FXML
    private Tab tabStatus;

    @FXML
    private Label name;

    @FXML
    private Label department;

    @FXML
    private Label cardNumber;

    @FXML
    private JFXButton btnChargeStatus;

    @FXML
    private JFXButton btnWorkStatus;

    @FXML
    private Tab tabHistory;

    @FXML
    private JFXListView<String> historyList;

    private UiPresenter uiPresenter;
    private MainView mainView;
    private int id;
    private int groupId;
    private int deviceId;

    DeviceCellView(UiPresenter uiPresenter, MainView mainView, int id) {
        this.uiPresenter = uiPresenter;
        this.mainView = mainView;
        this.id = id;
        loadFxml();
        tabStatus.setText(id + "号");
    }

    /**
     * device 对象不存在时，刷新显示 cell，显示设备为空
     *
     * @param groupId  组号
     * @param deviceId 设备号
     */
    void resetCell(int groupId, int deviceId) {
        Platform.runLater(() -> {
            this.groupId = groupId;
            this.deviceId = deviceId;
            name.setText("未命名");
            department.setText("无");
            cardNumber.setText("无");
        });
    }

    /**
     * device 对象存在时，刷新显示
     *
     * @param device 存在的 Device对象
     */
    public void refreshCell(Device device) {
        Platform.runLater(() -> {
            name.setText(device.getName());
            department.setText(device.getDepartment());
            cardNumber.setText(device.getCardNumber() + "");
            btnChargeStatus.setText(ChargeStatus.obtainDescription(device.getChargeStatus()));
            btnWorkStatus.setText(WorkStatus.obtainDescription(device.getWorkStatus()));
            historyList.getItems().clear();
            historyList.getItems().addAll(device.getHistoryList());
        });
    }

    @FXML
    void btnChargeAction(ActionEvent event) {
        Device device = hookedDeviceGetter.get();
        if (device != null) {
            int status = device.getChargeStatus();
            if (++status > ChargeStatus.FULL) {
                status = ChargeStatus.USING;
            }
            device.setChargeStatus(status);
            btnChargeStatus.setText(ChargeStatus.obtainDescription(status));
            sendMsgStatusCurrent(device);
        } else {
            nobindalertdialog();
        }
    }

    @FXML
    void btnWorkAction(ActionEvent event) {
        Device device = hookedDeviceGetter.get();
        if (device != null) {
            int status = device.getWorkStatus();
            if (++status > WorkStatus.CHARGING_TIME_OVER) {
                status = WorkStatus.NORMAL;
            } else if (status < WorkStatus.OVERCURRENT) {
                status = WorkStatus.OVERCURRENT;
            }
            device.setWorkStatus(status);
            btnWorkStatus.setText(WorkStatus.obtainDescription(status));
            sendMsgStatusCurrent(device);
        } else {
            nobindalertdialog();
        }
    }

    private void sendMsgStatusCurrent(Device device) {
        int groupId = device.getGroupId();
        int deviceId = device.getDeviceId();
        int chargeStatus = device.getChargeStatus();
        int workStatus = device.getWorkStatus();
        uiPresenter.sendMessage(MsgCodecReplyStatusCharge.create(groupId, deviceId, chargeStatus, System.currentTimeMillis()));
        uiPresenter.sendMessage(MsgCodecReplyStatusWork.create(groupId, deviceId, workStatus, System.currentTimeMillis()));
    }

    /**
     * 未绑定设备弹出警告
     */
    private void nobindalertdialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("警告");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("未关联相应的设备");
        alert.showAndWait();
    }

    private void loadFxml() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/device-cell.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新窗格和Device之间的钩子
     *
     * @param device 待更新的device
     */
    void refreshHook(Device device) {
        Device deviceOld = hookedDeviceGetter.get();
        if (deviceOld != null) {
            deviceOld.removeHook();
        }
        device.bindHook(this);
        hookedDeviceGetter.set(device);
    }
}
