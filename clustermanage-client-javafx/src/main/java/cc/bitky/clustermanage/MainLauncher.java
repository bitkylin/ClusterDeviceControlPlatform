package cc.bitky.clustermanage;

import cc.bitky.clustermanage.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainLauncher extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
           startApp(primaryStage);
    }

    private void startApp(Stage primaryStage) {
        primaryStage.setTitle("设备模拟客户端");
        primaryStage.setScene(new Scene(MainView.getInstance()));
        primaryStage.setMaximized(true);
        primaryStage.setOnCloseRequest(event -> NettyLauncher.getInstance().shutdown());
        primaryStage.show();
        MainView.getInstance().updateGroupCount(10);
    }
}
