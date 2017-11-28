package cc.bitky.clustermanage.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cc.bitky.clustermanage.KySetting;
import cc.bitky.clustermanage.NettyLauncher;
import cc.bitky.clustermanage.utils.ViewUtil;
import cc.bitky.clustermanage.view.bean.Device;
import cc.bitky.clustermanage.view.bean.DeviceGroup;
import cc.bitky.clustermanage.view.bean.DeviceKey;
import cc.bitky.clustermanage.view.viewbean.DeviceGroupListCell;
import cc.bitky.clustermanage.view.viewbean.DeviceView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class MainView extends BorderPane implements Initializable {
    private static MainView mainView;
    private ObservableList<DeviceGroup> deviceGroups;
    private int groupId;
    private MainViewActionListener listener;
    @FXML
    private ListView<DeviceGroup> deviceGroupList;
    @FXML
    private FlowPane deviceFlowPane;
    @FXML
    private Label labelCurrentGroupId;
    @FXML
    private Label labelConnStatus;
    @FXML
    private Label labelRecCount;
    @FXML
    private Button btnCleanRecCount;
    @FXML
    private MenuItem menuItemConn;
    @FXML
    private MenuItem menuItemSend;

    private MainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("rootLayout.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    public static MainView getInstance() {
        if (mainView == null) {
            try {
                mainView = new MainView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mainView;
    }

    public void setListener(MainViewActionListener listener) {
        this.listener = listener;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deviceGroups = FXCollections.observableArrayList();
        deviceGroupList.setCellFactory(param -> new DeviceGroupListCell());
        deviceGroupList.setItems(deviceGroups);
        deviceGroupList.getFocusModel().focusedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    groupId = newValue.getGroupId();
                    labelCurrentGroupId.setText(groupId + "组");
                    for (int i = 1; i <= 100; i++) {
                        Container.deviceViewHashMap.get(i).initView(Container.deviceHashMap.get(new DeviceKey(groupId, i)));
                    }
                });
        for (int i = 1; i <= 100; i++) {
            DeviceView deviceView = new DeviceView(i);
            deviceFlowPane.getChildren().add(deviceView);
            Container.deviceViewHashMap.put(i, deviceView);
            deviceView.setListener((tcpMsgResponseDeviceStatus -> {
                if (listener != null) listener.btnChargeChanged(tcpMsgResponseDeviceStatus);
            }));
        }
    }

    public void updateGroupCount(int groupId) {
        if (groupId > deviceGroups.size()) {
            for (int i = deviceGroups.size() + 1; i <= groupId; i++) {
                deviceGroups.add(new DeviceGroup(i));
                deviceGroupList.setItems(deviceGroups);
            }
        }
    }

    public void remoteUpdateDevice(int sum) {
        Platform.runLater(() -> labelRecCount.setText(sum + ""));
    }

    public void remoteUpdateDevice(int sum, Device device) {
        if (groupId != 0 && device != null && device.getGroupId() == groupId) {
            updateGroupCount(device.getGroupId());
            Platform.runLater(() -> {
                labelRecCount.setText(sum + "");
                Container.deviceViewHashMap.get(device.getDeviceId()).initView(device);
            });
        }
    }

    public void remoteUpdateDevice(int sum, Device... devices) {
        for (Device device : devices) {
            remoteUpdateDevice(sum, device);
        }
    }

    @FXML
    private void onActionMenuItemAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("关于");
        alert.setHeaderText("集群设备模拟客户端 " + KySetting.VERSION);
        alert.setGraphic(null);
        alert.setContentText(ViewUtil.getOsInfo());
        alert.showAndWait();
    }

    @FXML
    private void onActionMenuItemConn(ActionEvent event) {
        ViewUtil.ConnDialogResult().ifPresent(returnMsg -> {
            String hostName = returnMsg.getKey();
            String portStr = returnMsg.getValue();
            int port = 30232;
            if (hostName == null || hostName.equals("")) hostName = KySetting.HOST_NAME;
            try {
                if (portStr == null || portStr.equals("")) port = KySetting.PORT;
                else port = Integer.parseInt(portStr.trim());
            } catch (NumberFormatException e) {
                port = KySetting.PORT;
            }
            labelConnStatus.setText("连接中");
            NettyLauncher.getInstance().start(hostName, port);
        });
    }

    @FXML
    void onActionMenuItemSend(ActionEvent event) {
        ViewUtil.ResponseDeviceStatusResult().ifPresent(tcpMsgResponseDeviceStatus -> {
            if (listener != null) listener.btnChargeChanged(tcpMsgResponseDeviceStatus);
        });
    }

    @FXML
    void onActionMenuItemRandomSend(ActionEvent event) {
        ViewUtil.randomDeviceStatus().ifPresent(tcpMsgResponseDeviceStatus -> {
            if (listener != null) listener.btnRandomChargeChanged(tcpMsgResponseDeviceStatus);
        });
    }

    @FXML
    private void onActionBtnCleanRecCount(ActionEvent event) {
        listener.clearRecCount(true, false);
        labelRecCount.setText(0 + "");
    }

    public void setLabelConnStatus(boolean success) {
        Platform.runLater(() -> {
            if (success) {
                labelConnStatus.setText("已连接");
                menuItemConn.setText("已连接");
                menuItemConn.setDisable(true);
                menuItemSend.setDisable(false);
            } else {
                labelConnStatus.setText("已断开");
            }
        });
    }
}
