package cc.bitky.clustermanage.db.mongoops;

import com.mongodb.MongoClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class KyMongoConfig {
    @Bean
    public MongoClient mongoClient() {
        return new MongoClient("202.193.56.226");
    }

    @Bean
    public MongoOperations mongoTemplate() {
        return new MongoTemplate(mongoClient(), "bitky");
    }
}
