package cc.bitky.clusterdeviceplatform.client.ui;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXMasonryPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

public class MainView extends BorderPane {

    @FXML
    private JFXMasonryPane deviceMasonryPane;

    @FXML
    private JFXListView<String> deviceGroupList;

    public MainView() {
        loadFxml("/fxml/main-view.fxml");
        List<String> strings = new ArrayList<>(100);
        for (int i = 1; i <= 100; i++) {
            strings.add("第 " + i + " 组");
        }
        deviceGroupList.getItems().addAll(strings);
        List<DeviceCell> cells = new ArrayList<>(100);
        for (int i = 1; i <= 100; i++) {
            cells.add(new DeviceCell(i));
        }
        deviceMasonryPane.getChildren().addAll(cells);
    }

    private void loadFxml(String str) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(str));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
