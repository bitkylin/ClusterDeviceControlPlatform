package cc.bitky.clusterdeviceplatform.server.db.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CardSet")
public class CardSet {

    @Id
    private String id;
    private String[] cardList;

    public CardSet(String id, String[] cardList) {
        this.id = id;
        this.cardList = cardList;
    }

    public CardSet() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getCardList() {
        return cardList;
    }

    public void setCardList(String[] cardList) {
        this.cardList = cardList;
    }
}
