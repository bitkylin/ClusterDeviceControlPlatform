package cc.bitky.clusterdeviceplatform.client.ui.view;

import com.jfoenix.controls.JFXDecorator;

import cc.bitky.clusterdeviceplatform.client.ui.UiPresenter;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UiLauncher extends Application {

    private static UiPresenter uiPresenter;
    /**
     * 外部调用启动界面
     *
     * @param uiPresenter UI展示器
     * @param args        命令行参数
     */
    public static void runUi(UiPresenter uiPresenter, String... args) {
        UiLauncher.uiPresenter = uiPresenter;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        JFXDecorator decorator = new JFXDecorator(primaryStage, new MainView(uiPresenter));
        decorator.setCustomMaximize(true);
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(decorator);
        scene.getStylesheets().addAll(
                getClass().getResource("/css/main.css").toExternalForm(),
                getClass().getResource("/css/listview.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        uiPresenter.shutdown();
    }
}
