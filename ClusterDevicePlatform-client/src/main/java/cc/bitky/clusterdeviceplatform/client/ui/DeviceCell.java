package cc.bitky.clusterdeviceplatform.client.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;

public class DeviceCell extends StackPane {

    @FXML
    private Tab tabStatus;

    @FXML
    private Label name;

    @FXML
    private Label department;

    @FXML
    private Label cardNumber;

    @FXML
    private JFXButton btnCharge;

    @FXML
    private JFXButton btnWrong;

    @FXML
    private Tab tabHistory;

    @FXML
    private JFXListView<String> historyList;

    private int id;

    public DeviceCell(int id) {
        this.id = id;
        loadFxml("/fxml/device-cell.fxml");
        tabStatus.setText(id + "号");
        historyList.getItems().add("fdsfsd");
        historyList.getItems().add("fdsfsd");
        historyList.getItems().add("fdsrgfrfsd");
        historyList.getItems().add("fdgfsfsd");
        historyList.getItems().add("fdgdffsfsd");
        historyList.getItems().add("fdergfsfsd");
        historyList.getItems().add("fdgfsfsd");
        historyList.getItems().add("fdqgfsfsd");
    }

    public void refreshCell() {

    }

    @FXML
    void btnChargeAction(ActionEvent event) {

    }

    @FXML
    void btnWrongAction(ActionEvent event) {

    }

    public void init() {

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
