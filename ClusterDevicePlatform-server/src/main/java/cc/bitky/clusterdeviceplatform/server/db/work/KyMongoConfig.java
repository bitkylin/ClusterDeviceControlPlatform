package cc.bitky.clusterdeviceplatform.server.db.work;

import com.mongodb.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import cc.bitky.clusterdeviceplatform.server.config.DbSetting;

@Configuration
public class KyMongoConfig extends AbstractMongoConfiguration {

    @Bean
    public com.mongodb.reactivestreams.client.MongoClient reactiveMongoClient() {
        return MongoClients.create("mongodb://" + DbSetting.HOST);
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(reactiveMongoClient(), DbSetting.DATABASE);
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(DbSetting.HOST);
    }

    @Override
    protected String getDatabaseName() {
        return DbSetting.DATABASE;
    }

    @Override
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        MappingMongoConverter mmc = super.mappingMongoConverter();
        //remove _class
        mmc.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mmc;
    }
}
