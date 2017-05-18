package cc.bitky.clustermanage.view.viewbean;

import java.net.URL;
import java.util.ResourceBundle;

import cc.bitky.clustermanage.view.bean.DeviceGroup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class DeviceGroupListCell extends ListCell<DeviceGroup> implements Initializable {


    @FXML
    private Label label;

    @Override
    protected void updateItem(DeviceGroup item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText("第 " + item.getGroupId() + " 组");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
