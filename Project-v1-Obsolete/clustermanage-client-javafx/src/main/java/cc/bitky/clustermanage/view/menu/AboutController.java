package cc.bitky.clustermanage.view.menu;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

public class AboutController extends AnchorPane {
    @FXML
    private MenuItem menuItemClose;

    public AboutController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("about.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
