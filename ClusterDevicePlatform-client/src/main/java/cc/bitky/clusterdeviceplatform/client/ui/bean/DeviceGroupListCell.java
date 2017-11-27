package cc.bitky.clusterdeviceplatform.client.ui.bean;

import com.jfoenix.controls.JFXListCell;

public class DeviceGroupListCell extends JFXListCell<DeviceGroup> {

    @Override
    protected void updateItem(DeviceGroup item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setText("第 " + item.getGroupId() + " 组");
        }
    }
}
