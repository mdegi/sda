package mt.com.go.deploymentsmanagement.config;


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

    @Value("${fileNameRegex}")
    private String filenameRegex;

    @Value("${fileExtension}")
    private String fileExtension;

    @Value("${fileScanFixedRateMilliSeconds}")
    private  Integer fileScanFixedRateMilliSeconds;

    @Value("${fileScanInitialDelayMilliSeconds}")
    private  Integer fileScanInitialDelayMilliSeconds;

    @Value("${mongDBConnectDatabase}")
    private String dbName;

    @Value("${mongoDBURL}")
    private String dbURL;

    public MongoClient mongoClient() {
        String connectionString = dbURL;
        if (System.getProperty("mongoDBURL") != null) {
            connectionString = System.getProperty("mongoDBURL");
        }
        return MongoClients.create(connectionString);
    }

    public @Bean
    MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), dbName);
    }
}
