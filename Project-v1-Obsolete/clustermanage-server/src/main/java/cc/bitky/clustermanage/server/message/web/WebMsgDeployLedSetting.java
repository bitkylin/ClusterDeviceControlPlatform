package cc.bitky.clustermanage.server.message.web;

import cc.bitky.clustermanage.server.message.MsgType;
import cc.bitky.clustermanage.server.message.base.BaseMessage;
import cc.bitky.clustermanage.web.bean.LedSetting;

/**
 * 服务器部署员工卡号
 */
public class WebMsgDeployLedSetting extends BaseMessage {
    private Point point;
    private int duration;
    private int brightness;
    private String text;
    private byte bytePoint;

    private WebMsgDeployLedSetting(int groupId, int boxId) {
        super(groupId, boxId);
        setMsgId(MsgType.SERVER_LED_SETTING);
    }

    public WebMsgDeployLedSetting(int groupId, int deviceId, LedSetting ledSetting) {
        this(groupId, deviceId);
        point = ledSetting.getPoint();
        duration = ledSetting.duration;
        brightness = ledSetting.brightness;
        text = ledSetting.text;
    }

    public Point getPoint() {
        return point;
    }

    public int getDuration() {
        return duration;
    }

    public int getBrightness() {
        return brightness;
    }

    public String getText() {
        return text;
    }

    public byte getBytePoint() {
        switch (point) {
            case TOP:
                return 0x35;
            case BOTTOM:
                return 0x36;
            case LEFT:
                return 0x37;
            case RIGHT:
                return 0x38;
        }
        return 0x38;
    }

    public enum Point {
        LEFT,
        RIGHT,
        TOP,
        BOTTOM
    }
}
