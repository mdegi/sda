package com.md.sda.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Getter
@Component("appConfig")
public class AppConfig {

    @Value("${fileSystemPath:defaultPath}")
    private String fileSystemPath;

    @Value("${fileScanFixedRateMilliSeconds}")
    private  Integer fileScanFixedRateMilliSeconds;

    @Value("${fileScanInitialDelayMilliSeconds}")
    private  Integer fileScanInitialDelayMilliSeconds;

    @Value("${mongDBConectDatabase}")
    private String dbName;

    @Value("${mongoDBURL}")
    private String dbURL;

    public MongoClient mongoClient() {
        return MongoClients.create(dbURL);
    }

    public @Bean
    MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), dbName);
    }
}
