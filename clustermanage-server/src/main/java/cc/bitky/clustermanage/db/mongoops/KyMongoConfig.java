package cc.bitky.clustermanage.db.mongoops;

import com.mongodb.MongoClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import cc.bitky.clustermanage.ServerSetting;

@Configuration
public class KyMongoConfig {

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient(ServerSetting.HOST);
    }

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(mongoClient(), ServerSetting.DATABASE);
    }


//    public @Bean
//    MongoClientFactoryBean mongo() {
//        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
//        mongo.setHost("localhost");
//
//        return mongo;
//    }
//    @Bean
//    public MongoOperations mongoTemplate() {
//        return new MongoTemplate(mongoClient(), "bitky");
//    }
}
