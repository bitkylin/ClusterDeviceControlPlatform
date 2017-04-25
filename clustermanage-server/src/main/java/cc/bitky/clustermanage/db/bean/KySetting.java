package cc.bitky.clustermanage.db.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class KySetting {
    @Id
    private String id;

    private long[] freeCardList;
    private long[] confirmCardList;

    public long[] getConfirmCardList() {
        return confirmCardList;
    }

    public void setConfirmCardList(long[] confirmCardList) {
        this.confirmCardList = confirmCardList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long[] getFreeCardList() {
        return freeCardList;
    }

    public void setFreeCardList(long[] freeCardList) {
        this.freeCardList = freeCardList;
    }
}
