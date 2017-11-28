package cc.bitky.clustermanage.view.viewbean;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cc.bitky.clustermanage.netty.message.tcp.TcpMsgResponseDeviceStatus;
import cc.bitky.clustermanage.view.DeviceStatusChangeListener;
import cc.bitky.clustermanage.view.bean.Device;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;

public class DeviceView extends StackPane implements Initializable {

    private static Device deviceDefault = new Device();

    private DeviceStatusChangeListener listener;
    @FXML
    private Tab tabStatus;
    @FXML
    private Label name;
    @FXML
    private Label department;
    @FXML
    private Label cardNumber;
    @FXML
    private Button btnCharge;
    @FXML
    private Button btnWrong;
    @FXML
    private Tab tabHistory;
    @FXML
    private ListView<String> historyList;
    private int id;
    private Device device;

    public DeviceView(int id) {
        this.id = id;
        loadFxml();
        initView(null);
    }

    public void setListener(DeviceStatusChangeListener listener) {
        this.listener = listener;
    }

    public void initView(Device inDevice) {
        if (inDevice == null) {
            inDevice = deviceDefault;
        }
        this.device = inDevice;
        int setid = device.getDeviceId() == -1 ? this.id : device.getDeviceId();
        tabStatus.setText(setid + "号");
        name.setText(device.getName());
        department.setText(device.getDepartment());
        cardNumber.setText(device.getCardNumber() + "");
        deployBtnText(device.getStatus());
        historyList.getItems().clear();
        historyList.getItems().addAll(device.getHistoryList());
    }

    private void deployBtnText(int status) {
        if (status < 0) {
            btnCharge.setDisable(true);
            btnWrong.setDisable(true);
        } else {
            btnCharge.setDisable(false);
            btnWrong.setDisable(false);
        }
        switch (status) {
            case -1:
                btnCharge.setText("出厂");
                btnWrong.setText("好");
                break;
            case 0:
                btnCharge.setText("初始化");
                btnWrong.setText("好");
                break;
            case 1:
                btnCharge.setText("未充电");
                btnWrong.setText("好");
                break;
            case 2:
                btnCharge.setText("充电中");
                btnWrong.setText("好");
                break;
            case 3:
                btnCharge.setText("已充满");
                btnWrong.setText("好");
                break;
            case 4:
                btnCharge.setText("充电X");
                btnWrong.setText("好");
                break;
            case 5:
                btnCharge.setText("通信X");
                btnWrong.setText("坏");
                break;
            case 6:
                btnCharge.setText("多种X");
                btnWrong.setText("坏");
                break;
            default:
        }
    }

    private void loadFxml() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("device_view.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addList(String value) {
        historyList.getItems().add(value);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabStatus.setText(id + "号");
    }

    @FXML
    private void btnChargeAction(ActionEvent event) {
        int status = device.getStatus();

        if (status >= 0 && status <= 2) {
            status++;
        } else if (status == 3 || status == 4) {
            status = 1;
        } else if (status == 5) {
            status = 6;
        } else if (status == 6) {
            status = 5;
        } else {
            status = 0;
        }

        device.setStatus(status);
        deployBtnText(status);
        if (listener != null && status < 5) {
            listener.btnChargeChanged(new TcpMsgResponseDeviceStatus(device.getGroupId(), device.getDeviceId(), device.getStatus()));
        }
    }

    @FXML
    private void btnWrongAction(ActionEvent event) {
        int status = device.getStatus();
        if (status >= 5) {
            status = 0;
        } else if (status <= 4) {
            status = 5;
        }

        device.setStatus(status);
        deployBtnText(status);
        if (listener != null && status < 5) {
            listener.btnChargeChanged(new TcpMsgResponseDeviceStatus(device.getGroupId(), device.getDeviceId(), device.getStatus()));
        }
    }

    void setBtnWrongText(String value) {
        btnWrong.setText(value);
    }

    void setBtnChargeText(String value) {
        btnCharge.setText(value);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    public void setDepartment(String department) {
        this.department.setText(department);
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber.setText(cardNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DeviceView that = (DeviceView) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
