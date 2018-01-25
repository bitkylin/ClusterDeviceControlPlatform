package cc.bitky.clusterdeviceplatform.client.ui.view;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXMasonryPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.bitky.clusterdeviceplatform.client.config.DeviceSetting;
import cc.bitky.clusterdeviceplatform.client.ui.UiPresenter;
import cc.bitky.clusterdeviceplatform.client.ui.bean.Device;
import cc.bitky.clusterdeviceplatform.client.ui.bean.DeviceCellRepo;
import cc.bitky.clusterdeviceplatform.client.ui.bean.DeviceGroup;
import cc.bitky.clusterdeviceplatform.client.ui.bean.DeviceGroupListCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;

public class MainView extends StackPane {
    ObservableList<DeviceGroup> deviceGroups;
    ExecutorService service = Executors.newSingleThreadExecutor();
    @FXML
    private JFXMasonryPane deviceMasonryPane;
    @FXML
    private JFXListView<DeviceGroup> deviceGroupList;
    private cc.bitky.clusterdeviceplatform.client.ui.UiPresenter uiPresenter;

    MainView(UiPresenter uiPresenter) {
        this.uiPresenter = uiPresenter;
        loadFxml();
        deviceGroups = FXCollections.observableArrayList();
        deviceGroupList.setCellFactory(param -> new DeviceGroupListCell());
        deviceGroupList.setItems(deviceGroups);
        deviceGroupList.getFocusModel().focusedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            int groupId = newValue.getGroupId();
            uiPresenter.server.startClient(groupId);
            service.submit(() -> refreshDeviceCell(groupId));
        });
        List<DeviceGroup> groups = new ArrayList<>(DeviceSetting.MAX_GROUP_ID);
        for (int i = 1; i <= DeviceSetting.MAX_GROUP_ID; i++) {
            groups.add(new DeviceGroup(i));
        }
        deviceGroupList.getItems().addAll(groups);
    }

    /**
     * 切换显示的设备组，刷新一组设备的显示窗格
     *
     * @param groupId 设备组号
     */
    private void refreshDeviceCell(int groupId) {
        for (int deviceId = 1; deviceId <= DeviceSetting.MAX_DEVICE_ID; deviceId++) {
            DeviceCellView cellView = DeviceCellRepo.getCellView(deviceId);
            if (cellView == null) {
                DeviceCellView newDeviceCellView = new DeviceCellView(uiPresenter, this, deviceId);
                DeviceCellRepo.setCellView(newDeviceCellView, deviceId);
                newDeviceCellView.setPrefSize(150, 85);
                newDeviceCellView.resize(150, 85);
                Platform.runLater(() -> deviceMasonryPane.getChildren().add(newDeviceCellView));
                cellView = newDeviceCellView;
            }
            Device device = DeviceCellRepo.getDevice(groupId, deviceId);
            if (device == null) {
                cellView.resetCell(groupId, deviceId);
            } else {
                cellView.refreshHook(device);
                cellView.refreshCell(device);
            }
        }
    }

    private void loadFxml() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main-view.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
