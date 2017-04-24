package cc.bitky.clustermanage.db.mongoops;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RoutineTableOps {
    private final MongoOperations operations;

    @Autowired
    public RoutineTableOps(MongoOperations operations) {
        this.operations = operations;
    }
}
