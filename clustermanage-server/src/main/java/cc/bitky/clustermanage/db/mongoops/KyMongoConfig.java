package cc.bitky.clustermanage.db.mongoops;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import cc.bitky.clustermanage.global.ServerSetting;

@Configuration
public class KyMongoConfig extends AbstractMongoConfiguration {
//
//    @Bean
//    public MongoClient mongoClient() {
//        return new MongoClient(ServerSetting.HOST);
//    }

    @Bean
    @Override
    public MappingMongoConverter mappingMongoConverter() throws Exception {
        MappingMongoConverter mmc = super.mappingMongoConverter();
        //remove _class
        mmc.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mmc;
    }

    @Override
    protected String getDatabaseName() {
        return ServerSetting.DATABASE;
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient(ServerSetting.HOST);
    }
}
