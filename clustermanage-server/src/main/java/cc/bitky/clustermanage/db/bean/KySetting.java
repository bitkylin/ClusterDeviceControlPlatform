package cc.bitky.clustermanage.db.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class KySetting {
    @Id
    private String id;

    public String[] getFreeCardList() {
        return freeCardList;
    }

    public void setFreeCardList(String[] freeCardList) {
        this.freeCardList = freeCardList;
    }

    public String[] getConfirmCardList() {
        return confirmCardList;
    }

    public void setConfirmCardList(String[] confirmCardList) {
        this.confirmCardList = confirmCardList;
    }

    private String[] freeCardList;
    private String[] confirmCardList;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

 }
