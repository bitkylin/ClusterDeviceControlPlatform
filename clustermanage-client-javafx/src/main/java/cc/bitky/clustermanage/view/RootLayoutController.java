package cc.bitky.clustermanage.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;

public class RootLayoutController {
  @FXML
  private MenuItem menuItemClose;


  @FXML
  private void handleAbout() {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("警告");
    alert.setHeaderText(null);
    alert.setContentText("请选择一个条目!");

    alert.showAndWait();
  }
}
