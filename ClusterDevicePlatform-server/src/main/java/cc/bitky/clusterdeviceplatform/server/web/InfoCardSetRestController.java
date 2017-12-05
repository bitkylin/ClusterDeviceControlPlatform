package cc.bitky.clusterdeviceplatform.server.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardClear;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardConfirm;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardFree;
import cc.bitky.clusterdeviceplatform.server.db.bean.CardSet;
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
        return queryCards(CardType.Free);
    }

    /**
     * 从数据库中获取确认卡号的集合
     *
     * @return 确认卡号的集合
     */
    @GetMapping("/confirmcard")
    public Mono<String[]> queryConfirmCard() {
        return queryCards(CardType.Confirm);
    }

    /**
     * 从数据库中获取清除卡号的集合
     *
     * @return 清除卡号的集合
     */
    @GetMapping("/clearcard")
    public Mono<String[]> queryClearCards() {
        return queryCards(CardType.Clear);
    }

    /**
     * 部署万能卡号的集合
     *
     * @return 万能卡号的集合
     */
    @PostMapping(path = "/freecard/{groupId}/{deviceId}", consumes = "application/json")
    public String deployFreeCards(@PathVariable int groupId, @PathVariable int deviceId, @RequestBody String[] cards) {
        CardSet cardSet = saveCards(cards, CardType.Free).block();
        if (sendCardSetGrouped(groupId, deviceId, cardSet.getCardList(), CardType.Free)) {
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
    public String deployConfirmCard(@PathVariable int groupId, @RequestBody String[] cards) {
        CardSet cardSet = saveCards(cards, CardType.Confirm).block();
        if (sendCardSetGrouped(groupId, 0, cardSet.getCardList(), CardType.Confirm)) {
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
    public String deployClearCards(@PathVariable int groupId, @RequestBody String[] cards) {
        CardSet cardSet = saveCards(cards, CardType.Clear).block();
        if (sendCardSetGrouped(groupId, 0, cardSet.getCardList(), CardType.Clear)) {
            return "success";
        } else {
            return "error";
        }
    }

    /**
     * 根据类型获取指定的卡号组
     *
     * @param type 卡号组的类型
     * @return 特定的卡号组
     */
    private Mono<String[]> queryCards(CardType type) {
        return serverProcessor.getDbPresenter().queryCardSet(type);
    }

    /**
     * 保存特定的卡号组
     *
     * @param cards 特定的卡号组
     * @param type  卡号组的类型
     * @return 保存成功
     */
    private Mono<CardSet> saveCards(String[] cards, CardType type) {
        return serverProcessor.getDbPresenter().saveCardSet(cards, type);
    }

    /**
     * 向设备部署卡号的集合
     *
     * @param groupId  设备组号
     * @param deviceId 设备号
     * @param cards    卡号字符串的集合
     * @param type     卡号类型
     * @return 部署成功
     */
    private boolean sendCardSetGrouped(int groupId, int deviceId, String[] cards, CardType type) {
        List<Integer> cardInt = new ArrayList<>(cards.length);
        for (String card : cards) {
            cardInt.add(Integer.parseUnsignedInt(card, 16));
        }

        switch (type) {
            case Free:
                return serverProcessor.sendMessageGrouped(MsgCodecCardFree.create(groupId, deviceId, cardInt));
            case Confirm:
                return serverProcessor.sendMessageGrouped(MsgCodecCardConfirm.create(groupId, deviceId, cardInt));
            case Clear:
                return serverProcessor.sendMessageGrouped(MsgCodecCardClear.create(groupId, deviceId, cardInt));
            default:
                return false;
        }
    }
}
