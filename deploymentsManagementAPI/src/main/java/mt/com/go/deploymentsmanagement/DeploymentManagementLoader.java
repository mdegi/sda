package mt.com.go.deploymentsmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

@SpringBootApplication(exclude={MongoAutoConfiguration.class})
public class DeploymentManagementLoader {

	public static void main(String[] args) {
		SpringApplication.run(DeploymentManagementLoader.class, args);
	}

}
