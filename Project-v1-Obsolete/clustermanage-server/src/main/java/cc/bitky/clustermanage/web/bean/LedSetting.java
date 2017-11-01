package cc.bitky.clustermanage.web.bean;

import cc.bitky.clustermanage.server.message.web.WebMsgDeployLedSetting;

public class LedSetting {
    public int duration;
    public int brightness;
    public String text;
    public String point;

    public boolean isEnabled() {
        if (duration < 0 || duration > 9)
            return false;
        if (brightness < 1 || brightness > 16)
            return false;
        return point != null && text != null;
    }

    public WebMsgDeployLedSetting.Point getPoint() {
        switch (point) {
            case "上":
                return WebMsgDeployLedSetting.Point.TOP;
            case "下":
                return WebMsgDeployLedSetting.Point.BOTTOM;
            case "左":
                return WebMsgDeployLedSetting.Point.LEFT;
            case "右":
                return WebMsgDeployLedSetting.Point.RIGHT;
        }
        return WebMsgDeployLedSetting.Point.RIGHT;
    }
}
