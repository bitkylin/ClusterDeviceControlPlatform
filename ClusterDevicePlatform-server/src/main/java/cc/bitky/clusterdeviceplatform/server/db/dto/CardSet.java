package cc.bitky.clusterdeviceplatform.server.db.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author limingliang
 */
@Data
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
}
