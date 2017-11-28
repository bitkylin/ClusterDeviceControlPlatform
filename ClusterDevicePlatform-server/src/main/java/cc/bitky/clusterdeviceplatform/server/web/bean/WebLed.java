package cc.bitky.clusterdeviceplatform.server.web.bean;

import cc.bitky.clusterdeviceplatform.messageutils.msg.led.MsgLedOn;

public class WebLed {
    private int duration;
    private int brightness;
    private String text;
    private String point;

    public boolean isEnabled() {
        if (duration < 0 || duration > 9) {
            return false;
        }
        if (brightness < 1 || brightness > 16) {
            return false;
        }
        return point != null && text != null && getPoint() != MsgLedOn.Point.EXCEPTION;
    }

    public MsgLedOn.Point getPoint() {
        switch (point) {
            case "上":
                return MsgLedOn.Point.UP;
            case "下":
                return MsgLedOn.Point.DOWN;
            case "左":
                return MsgLedOn.Point.LEFT;
            case "右":
                return MsgLedOn.Point.RIGHT;
            default:
                return MsgLedOn.Point.EXCEPTION;
        }
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
