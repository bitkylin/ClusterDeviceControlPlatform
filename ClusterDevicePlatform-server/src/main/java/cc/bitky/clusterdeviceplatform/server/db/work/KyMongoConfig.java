package cc.bitky.clusterdeviceplatform.server.db.work;

import cc.bitky.clusterdeviceplatform.server.config.DbSetting;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

/**
 * @author limingliang
 */
@Configuration
public class KyMongoConfig {

    @Bean
    public MongoClient reactiveMongoClient() {
        if (DbSetting.databaseAuthenticationStatus) {
            return MongoClients.create("mongodb://" + DbSetting.databaseUsername + ":" + DbSetting.databasePassword + "@" + DbSetting.mongodbHost + ":" + DbSetting.mongodbPort + "/" + DbSetting.database);
        } else {
            return MongoClients.create("mongodb://" + DbSetting.mongodbHost + ":" + DbSetting.mongodbPort);
        }
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
    }

    private String getDatabaseName() {
        return DbSetting.database;
    }
}
