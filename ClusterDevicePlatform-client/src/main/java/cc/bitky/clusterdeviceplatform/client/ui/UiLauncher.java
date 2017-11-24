package cc.bitky.clusterdeviceplatform.client.ui;

import com.jfoenix.controls.JFXDecorator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class UiLauncher extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        BorderPane root = new MainView();
        JFXDecorator decorator = new JFXDecorator(primaryStage, root);
        decorator.setCustomMaximize(true);
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(decorator);
        scene.getStylesheets().addAll(
                getClass().getResource("/css/main.css").toExternalForm(),
                getClass().getResource("/css/listview.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
