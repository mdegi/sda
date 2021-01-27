package mt.com.go.deploymentsmanagement.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import mt.com.go.ril.auth.token.properties.ExternalPropertiesGoTokenSecurityConfig;
import mt.com.go.ril.error.ErrorConfig;
import org.apache.catalina.valves.AccessLogValve;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableAutoConfiguration
@Import({ErrorConfig.class, ExternalPropertiesGoTokenSecurityConfig.class})
@PropertySources(value = {
        @PropertySource(value = "file:config/security.properties")
})
@Getter
@Component("appConfig")
@EnableWebMvc
public class AppConfig {

    private final String DB_PROPERTY = "mongoDBURL";

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

    @Value("${server.port}")
    private int serverPort;

    public MongoClient mongoClient() {
        return (System.getProperty(DB_PROPERTY) != null) ? MongoClients.create(System.getProperty(DB_PROPERTY)) : MongoClients.create(dbURL);
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoClient(), dbName);
    }

    @Bean
    public TomcatServletWebServerFactory containerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.setPort(serverPort);
        tomcat.addContextValves(accessLogger());

        return tomcat;
    }

    @Bean
    public AccessLogValve accessLogger() {
        AccessLogValve valve = new AccessLogValve();
        valve.setDirectory("logs/access");
        valve.setPattern("common");
        valve.setSuffix(".log");
        return valve;
    }

}
