package cc.bitky.clustermanage;

import cc.bitky.clustermanage.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainLauncher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Stage primaryStage1 = primaryStage;
        primaryStage1.setTitle("bitkyApp");
        primaryStage.setScene(new Scene(MainView.getInstance()));
        primaryStage.setMaximized(true);
        primaryStage.show();
        MainView.getInstance().updateGroupCount(1);
     //   new App().start();
    }
}
