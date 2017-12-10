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
import java.util.Optional;

import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardClear;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardConfirm;
import cc.bitky.clusterdeviceplatform.messageutils.msgcodec.card.MsgCodecCardFree;
import cc.bitky.clusterdeviceplatform.server.config.DbSetting;
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
    @GetMapping("/freecard/")
    public String[] queryFreeCards() {
        return queryCards(CardType.Free).orElse(new String[0]);
    }

    /**
     * 从数据库中获取确认卡号的集合
     *
     * @return 确认卡号的集合
     */
    @GetMapping("/confirmcard")
    public String[] queryConfirmCard() {
        return queryCards(CardType.Confirm).orElse(new String[0]);
    }

    /**
     * 从数据库中获取清除卡号的集合
     *
     * @return 清除卡号的集合
     */
    @GetMapping("/clearcard")
    public String[] queryClearCards() {
        return queryCards(CardType.Clear).orElse(new String[0]);
    }

    /**
     * 保存万能卡号的集合
     *
     * @return 万能卡号的集合
     */
    @PostMapping(path = "/freecard", consumes = "application/json")
    public String saveFreeCards(@RequestBody String[] cards) {
        saveCards(cards, CardType.Free).block();
        return "success";
    }


    /**
     * 保存确认卡号的集合
     *
     * @return 确认卡号的集合
     */
    @PostMapping(path = "/confirmcard", consumes = "application/json")
    public String saveConfirmCard(@RequestBody String[] cards) {
        saveCards(cards, CardType.Confirm).block();
        return "success";
    }

    /**
     * 保存清除卡号的集合
     *
     * @return 清除卡号的集合
     */
    @PostMapping(path = "/clearcard", consumes = "application/json")
    public String saveClearCards(@RequestBody String[] cards) {
        saveCards(cards, CardType.Clear).block();
        return "success";
    }

    /**
     * 部署万能卡号的集合
     *
     * @return 万能卡号的集合
     */
    @GetMapping(path = "/freecard/deploy/{groupId}/{deviceId}")
    public String deployFreeCards(@PathVariable int groupId, @PathVariable int deviceId) {
        String[] cardSet = queryCards(CardType.Free).orElse(new String[]{DbSetting.DEFAULT_EMPLOYEE_CARD_NUMBER});
        if (sendCardSetGrouped(groupId, deviceId, cardSet, CardType.Free)) {
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
    @GetMapping(path = "/confirmcard/deploy/{groupId}")
    public String deployConfirmCard(@PathVariable int groupId) {
        String[] cardSet = queryCards(CardType.Confirm).orElse(new String[]{DbSetting.DEFAULT_EMPLOYEE_CARD_NUMBER});
        if (sendCardSetGrouped(groupId, 0, cardSet, CardType.Confirm)) {
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
    @GetMapping(path = "/clearcard/deploy/{groupId}")
    public String deployClearCards(@PathVariable int groupId) {
        String[] cardSet = queryCards(CardType.Clear).orElse(new String[]{DbSetting.DEFAULT_EMPLOYEE_CARD_NUMBER});
        if (sendCardSetGrouped(groupId, 0, cardSet, CardType.Clear)) {
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
    private Optional<String[]> queryCards(CardType type) {
        CardSet cardSet = serverProcessor.getDbPresenter().queryCardSet(type).orElse(new CardSet());
        return Optional.ofNullable(cardSet.getCardList());
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
