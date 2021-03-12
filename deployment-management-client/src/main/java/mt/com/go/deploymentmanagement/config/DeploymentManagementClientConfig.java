package mt.com.go.deploymentmanagement.config;

import mt.com.go.deploymentmanagement.client.DeploymentManagementClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DeploymentManagementClientConfig {

    @Value("${deploymentmanagement.client.publickey}")
    String publicKey;

    @Value("${deploymentmanagement.client.privatekey}")
    String privateKey;

    @Value("${deploymentmanagement.client.rooturl}")
    String rootUrl;

    @Value("${deploymentmanagement.client.timeout:-1}")
    int timeout;

    @Autowired
    RestTemplate restTemplate;

    @Bean
    public DeploymentManagementClient deploymentManagementClient() {
        return timeout == -1
                ? new DeploymentManagementClient(publicKey, privateKey, rootUrl, restTemplate)
                : new DeploymentManagementClient(publicKey, privateKey, rootUrl, timeout, restTemplate);
    }


}
