package cc.bitky.clustermanage.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import cc.bitky.clustermanage.db.bean.Device;
import cc.bitky.clustermanage.server.bean.ServerWebMessageHandler;
import cc.bitky.clustermanage.server.message.CardType;

/**
 * 设备信息获取及处理控制器
 */
@RestController
@RequestMapping(value = "/info")
public class InfoRestController {

    private final ServerWebMessageHandler serverWebMessageHandler;
    private Logger logger = LoggerFactory.getLogger(InfoRestController.class);

    @Autowired
    public InfoRestController(ServerWebMessageHandler serverWebMessageHandler) {
        this.serverWebMessageHandler = serverWebMessageHandler;
    }

    /**
     * 从数据库中获取设备的集合
     *
     * @param groupId  设备组 ID
     * @param deviceId 设备 ID
     * @return 设备集合
     */
    @RequestMapping(value = "/devices/{groupId}/{deviceId}", method = RequestMethod.GET)
    public List<Device> getDevices(@PathVariable int groupId, @PathVariable int deviceId) {
        return serverWebMessageHandler.getDeviceInfo(groupId, deviceId);
    }

    /**
     * 从数据库中获取万能卡号的集合
     *
     * @return 万能卡号的集合
     */
    @RequestMapping(value = "/freecard", method = RequestMethod.GET)
    public long[] obtainFreeCards() {
        return serverWebMessageHandler.obtainFreeCards();
    }

    /**
     * 从数据库中获取确认卡号的集合
     *
     * @return 确认卡号的集合
     */
    @RequestMapping(value = "/confirmcard", method = RequestMethod.GET)
    public long[] obtainConfirmCard() {
        return serverWebMessageHandler.obtainConfirmCards();
    }

    /**
     * 保存确认卡号到数据库
     *
     * @param freecards 确认卡号数组
     * @return @return "保存确认卡号成功"消息
     */
    @RequestMapping(value = "/confirmcard", method = RequestMethod.POST, consumes = "application/json")
    public String saveConfirmCard(@RequestBody long[] freecards) {
        if (serverWebMessageHandler.saveCardNumber(freecards, CardType.CONFIRM)) return "success";
        return "error";
    }


    /**
     * 保存万能卡号到数据库
     *
     * @param freecards 万能卡号数组
     * @return "保存万能卡号成功"消息
     */
    @RequestMapping(value = "/freecard", method = RequestMethod.POST, consumes = "application/json")
    public String saveFreeCard(@RequestBody long[] freecards) {
        if (serverWebMessageHandler.saveCardNumber(freecards, CardType.FREE)) return "success";
        return "error";
    }
}
