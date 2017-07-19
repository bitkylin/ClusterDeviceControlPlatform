package cc.bitky.clustermanage.utils;

import java.util.Optional;
import java.util.Properties;

import cc.bitky.clustermanage.netty.message.TcpMsgResponseRandomDeviceStatus;
import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseDeviceStatus;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;


public class ViewUtil {
    /**
     * 获取操作系统信息
     *
     * @return 生成的信息
     */
    public static String getOsInfo() {
        Properties props = System.getProperties();
        return "Java运行时版本: " + props.getProperty("java.runtime.version") + "\n" +
                "操作系统: " + props.getProperty("os.name") + "\n" +
                "架构: " + props.getProperty("os.arch") + "\n" +
                "语言: " + props.getProperty("user.language") + "\n" +
                "用户名: " + props.getProperty("user.name") + "\n" +
                "系统编码: " + props.getProperty("sun.jnu.encoding") + "\n" +
                "文件编码: " + props.getProperty("file.encoding") + "\n";
    }

    public static Optional<Pair<String, String>> ConnDialogResult() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("建立连接");
        dialog.setHeaderText("请输入服务器的连接信息");


        ButtonType loginButtonType = new ButtonType("连接", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField hostName = new TextField();
        hostName.setPromptText("localhost");
        hostName.setText("localhost");
        TextField port = new TextField();
        port.setPromptText("30232");
        port.setText("30232");

        grid.add(new Label("主机名: "), 0, 0);
        grid.add(hostName, 1, 0);
        grid.add(new Label("端口号: "), 0, 1);
        grid.add(port, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        //       Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        //  loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
//        hostName.textProperty().addListener((observable, oldValue, newValue) -> {
//            loginButton.setDisable(newValue.trim().isEmpty());
//        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> hostName.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(hostName.getText(), port.getText());
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static Optional<TcpMsgResponseDeviceStatus> ResponseDeviceStatusResult() throws NumberFormatException {
        Dialog<TcpMsgResponseDeviceStatus> dialog = new Dialog<>();
        dialog.setTitle("发送状态信息");
        dialog.setHeaderText("请设置单个设备的状态");


        ButtonType loginButtonType = new ButtonType("发送", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField textFieldGroupId = new TextField();
        textFieldGroupId.setPromptText("1 - 120");

        TextField textFieldDeviceId = new TextField();
        textFieldDeviceId.setPromptText("1 - 100");

        TextField textFieldStatus = new TextField();
        textFieldStatus.setPromptText("1 - 6");


        grid.add(new Label("组号: "), 0, 0);
        grid.add(textFieldGroupId, 1, 0);
        grid.add(new Label("设备号: "), 0, 1);
        grid.add(textFieldDeviceId, 1, 1);
        grid.addRow(2, new Label("状态码: "));
        //      grid.add(, 0, 2);
        grid.add(textFieldStatus, 1, 2);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        textFieldGroupId.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(fieldisEmpty(textFieldGroupId, textFieldDeviceId, textFieldStatus)));
        textFieldDeviceId.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(fieldisEmpty(textFieldGroupId, textFieldDeviceId, textFieldStatus)));
        textFieldStatus.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(fieldisEmpty(textFieldGroupId, textFieldDeviceId, textFieldStatus)));

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(textFieldGroupId::requestFocus);

        dialog.setResultConverter(dialogButton -> {

            if (dialogButton == loginButtonType) {
                try {
                    TcpMsgResponseDeviceStatus tcpMsgResponseDeviceStatus = new TcpMsgResponseDeviceStatus(Integer.parseInt(
                            textFieldGroupId.getText().trim()),
                            Integer.parseInt(textFieldDeviceId.getText().trim()),
                            Integer.parseInt(textFieldStatus.getText().trim()));
                    return tcpMsgResponseDeviceStatus;
                } catch (NumberFormatException e) {
                    System.out.println("空");
                    return new TcpMsgResponseDeviceStatus(-1, -1, -1);
                }
            }
            return null;
        });
        return dialog.showAndWait();
    }

    public static Optional<TcpMsgResponseRandomDeviceStatus> randomDeviceStatus() throws NumberFormatException {
        Dialog<TcpMsgResponseRandomDeviceStatus> dialog = new Dialog<>();
        dialog.setTitle("随机状态信息");
        dialog.setHeaderText("随机设备的状态信息");

        ButtonType loginButtonType = new ButtonType("发送", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField textFieldGroupId = new TextField();
        textFieldGroupId.setPromptText("1 - 120");

        TextField textFieldLength = new TextField();
        textFieldLength.setPromptText("1 - 60_0000");

        TextField textFieldStatus = new TextField();
        textFieldStatus.setPromptText("1 - 6");


        grid.add(new Label("组号: "), 0, 0);
        grid.add(textFieldGroupId, 1, 0);
        grid.add(new Label("范围: "), 0, 1);
        grid.add(textFieldLength, 1, 1);
        grid.addRow(2, new Label("状态码: "));
        //      grid.add(, 0, 2);
        grid.add(textFieldStatus, 1, 2);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        textFieldGroupId.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(fieldisEmpty(textFieldGroupId, textFieldLength, textFieldStatus)));
        textFieldLength.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(fieldisEmpty(textFieldGroupId, textFieldLength, textFieldStatus)));
        textFieldStatus.textProperty().addListener((observable, oldValue, newValue) -> loginButton.setDisable(fieldisEmpty(textFieldGroupId, textFieldLength, textFieldStatus)));

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(textFieldGroupId::requestFocus);

        dialog.setResultConverter(dialogButton -> {

            if (dialogButton == loginButtonType) {
                try {
                    TcpMsgResponseRandomDeviceStatus tcpMsgResponseDeviceStatus = new TcpMsgResponseRandomDeviceStatus(Integer.parseInt(
                            textFieldGroupId.getText().trim()),
                            Integer.parseInt(textFieldStatus.getText().trim()),
                            Integer.parseInt(textFieldLength.getText().trim()));
                    return tcpMsgResponseDeviceStatus;
                } catch (NumberFormatException e) {
                    System.out.println("空");
                    return new TcpMsgResponseRandomDeviceStatus(-1, -1, -1);
                }
            }
            return null;
        });
        return dialog.showAndWait();
    }


    private static boolean fieldisEmpty(TextField textFieldGroupId, TextField textFieldStatus) {
        return textFieldGroupId.getText().trim().isEmpty()
                || textFieldStatus.getText().trim().isEmpty();
    }

    private static boolean fieldisEmpty(TextField textFieldGroupId, TextField textFieldDeviceId, TextField textFieldStatus) {
        return textFieldGroupId.getText().trim().isEmpty()
                || textFieldStatus.getText().trim().isEmpty()
                || textFieldDeviceId.getText().trim().isEmpty();
    }
}
