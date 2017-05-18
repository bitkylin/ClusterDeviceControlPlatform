package cc.bitky.clustermanage.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cc.bitky.clustermanage.view.bean.Device;
import cc.bitky.clustermanage.view.bean.DeviceGroup;
import cc.bitky.clustermanage.view.bean.DeviceKey;
import cc.bitky.clustermanage.view.viewbean.DeviceGroupListCell;
import cc.bitky.clustermanage.view.viewbean.DeviceView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class MainView extends AnchorPane implements Initializable {
    private static MainView mainView;
    ObservableList<DeviceGroup> deviceGroups;
    int groupId;
    private KyDeviceViewListener listener;
    @FXML
    private ListView<DeviceGroup> deviceGroupList;
    @FXML
    private FlowPane deviceFlowPane;
    @FXML
    private SplitPane splitPane;

    private MainView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainview.fxml"));
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

    public void setListener(KyDeviceViewListener listener) {
        this.listener = listener;
    }

    public DeviceView getDevice(int deviceId) {
        return Container.deviceViewHashMap.get(deviceId);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        deviceGroups = FXCollections.observableArrayList();
        deviceGroupList.setCellFactory(param -> new DeviceGroupListCell());
        deviceGroupList.setItems(deviceGroups);
        deviceGroupList.getFocusModel().focusedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    groupId = newValue.getGroupId();
                    for (int i = 1; i <= 100; i++) {
                        Container.deviceViewHashMap.get(i).initView(Container.deviceHashMap.get(new DeviceKey(groupId, i)));
                    }
                });

        for (int i = 1; i <= 100; i++) {
            DeviceView deviceView = new DeviceView(i);
            deviceFlowPane.getChildren().add(deviceView);
            Container.deviceViewHashMap.put(i, deviceView);
            deviceView.setListener((groupId, deviceId, status) -> {
                if (listener != null) listener.btnChargeChanged(groupId, deviceId, status);
            });
        }


    }

    public void updateGroupCount(int groupid) {
        if (groupid > deviceGroups.size()) {
            for (int i = deviceGroups.size() + 1; i <= groupid; i++) {
                deviceGroups.add(new DeviceGroup(i));
                deviceGroupList.setItems(deviceGroups);
            }
        }
    }

    public void remoteUpdateDevice(Device device) {
        if (groupId != 0 && device != null && device.getGroupId() == groupId) {
            updateGroupCount(device.getGroupId());
            Container.deviceViewHashMap.get(device.getDeviceId()).initView(device);
        }
    }

    public void remoteUpdateDevice(Device... devices) {
        for (Device device : devices) {
            remoteUpdateDevice(device);
        }
    }
}
