package cc.bitky.clusterdeviceplatform.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cc.bitky.clusterdeviceplatform.server.server.ServerWebProcessor;
import cc.bitky.clusterdeviceplatform.server.web.bean.CardType;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/info/cardset")
public class InfoCardSetRestController {

    private final ServerWebProcessor serverProcessor;

    @Autowired
    public InfoCardSetRestController(ServerWebProcessor serverProcessor) {
        this.serverProcessor = serverProcessor;
    }

    /**
     * 从数据库中获取万能卡号的集合
     *
     * @return 万能卡号的集合
     */
    @GetMapping("/freecard")
    public Mono<String[]> queryFreeCards() {
        return serverProcessor.queryCards(CardType.Free);
    }

    /**
     * 从数据库中获取确认卡号的集合
     *
     * @return 确认卡号的集合
     */
    @GetMapping("/confirmcard")
    public Mono<String[]> queryConfirmCard() {
        return serverProcessor.queryCards(CardType.Confirm);
    }

    /**
     * 从数据库中获取清除卡号的集合
     *
     * @return 清除卡号的集合
     */
    @GetMapping("/clearcard")
    public Mono<String[]> queryClearCards() {
        return serverProcessor.queryCards(CardType.Clear);
    }

//    /**
//     * 保存确认卡号到数据库
//     *
//     * @param confirmCards 确认卡号数组
//     * @return @return "保存确认卡号成功"消息
//     */
//    @PostMapping(path = "/confirmcard", consumes = "application/json")
//    public String saveConfirmCard(@RequestBody String[] confirmCards) {
//        boolean success = serverProcessor.saveCards(confirmCards, CardType.Confirm);
//        if (success) {
//            return "success";
//        } else {
//            return "error";
//        }
//    }
//
//    /**
//     * 保存万能卡号到数据库
//     *
//     * @param freeCards 万能卡号数组
//     * @return @return "保存万能卡号成功"消息
//     */
//    @PostMapping(path = "/freecard", consumes = "application/json")
//    public String saveFreeCard(@RequestBody String[] freeCards) {
//        boolean success = serverProcessor.saveCards(freeCards, CardType.Free);
//        if (success) {
//            return "success";
//        } else {
//            return "error";
//        }
//    }
//
//    /**
//     * 保存清除卡号到数据库
//     *
//     * @param clearCards 清除卡号数组
//     * @return @return "保存清除卡号成功"消息
//     */
//    @PostMapping(path = "/clearcard", consumes = "application/json")
//    public String saveClearCard(@RequestBody String[] clearCards) {
//        boolean success = serverProcessor.saveCards(clearCards, CardType.Clear);
//        if (success) {
//            return "success";
//        } else {
//            return "error";
//        }
//    }

    /**
     * 部署万能卡号的集合
     *
     * @return 万能卡号的集合
     */
    @PostMapping(path = "/freecard/{groupId}/{deviceId}", consumes = "application/json")
    public String deployFreeCards(@PathVariable int groupId, @PathVariable int deviceId, @RequestBody String[] cards) {
        if (!serverProcessor.saveCards(cards, CardType.Free)) {
            return "error";
        }
        if (serverProcessor.ungroupCardSet(groupId, deviceId, CardType.Free)) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 部署确认卡号的集合
     *
     * @return 确认卡号的集合
     */
    @PostMapping(path = "/confirmcard/{groupId}", consumes = "application/json")
    public String deployConfirmCard(@PathVariable int groupId, @PathVariable int deviceId, @RequestBody String[] cards) {
        if (!serverProcessor.saveCards(cards, CardType.Confirm)) {
            return "error";
        }
        if (serverProcessor.ungroupCardSet(groupId, 0, CardType.Confirm)) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 部署清除卡号的集合
     *
     * @return 清除卡号的集合
     */
    @PostMapping(path = "/clearcard/{groupId}", consumes = "application/json")
    public String deployClearCards(@PathVariable int groupId, @PathVariable int deviceId, @RequestBody String[] cards) {
        if (!serverProcessor.saveCards(cards, CardType.Clear)) {
            return "error";
        }
        if (serverProcessor.ungroupCardSet(groupId, 0, CardType.Clear)) {
            return "success";
        } else {
            return "error";
        }
    }

}
