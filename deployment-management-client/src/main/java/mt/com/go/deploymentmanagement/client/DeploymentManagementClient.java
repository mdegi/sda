package mt.com.go.deploymentmanagement.client;

import mt.com.go.deploymentmanagement.models.DeploymentRequest;
import mt.com.go.deploymentmanagement.models.SystemDeployment;
import mt.com.go.ril.error.exception.go.GoRestTemplateException;
import mt.com.go.ril.integration.GoRestTemplate;
import mt.com.go.ril.integration.Header;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

public class DeploymentManagementClient {

    private RestTemplate restTemplate;

    private String rootUrl;

    private GoRestTemplate goRestTemplate;

    private static final String SERVICE_RESOURCE = "/api/deploymentsmanagement/v1/deployments";
    private final String DEPLOYMENTS_BY_DATE_URL = SERVICE_RESOURCE + "/deploymentDate:{deploymentDate}";
    private final String DEPLOYMENTS_BY_POST_DEPLOYMENT_STATUS_URL = SERVICE_RESOURCE +  "/postDeploymentTaskStatus:{deploymentStatus}";
    private final String DEPLOYMENTS_TOTAL_DURATION_URL = SERVICE_RESOURCE +  "/deploymentDurationByDate:{deploymentDate}";
    private final String DEPLOYMENTS_BY_SYSTEM_URL = SERVICE_RESOURCE +  "/system:{systemName}";

    public DeploymentManagementClient(String publicKey, String privateKey, String rootUrl, RestTemplate restTemplate) {
        this.rootUrl = rootUrl;
        this.restTemplate = restTemplate;
        this.restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()));
        this.goRestTemplate = new GoRestTemplate(publicKey, privateKey, restTemplate);
    }

    public DeploymentManagementClient(String publicKey, String privateKey, String rootUrl, int timeout, RestTemplate restTemplate) {
        this.rootUrl = rootUrl;
        this.restTemplate = restTemplate;
        this.restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(new HttpComponentsClientHttpRequestFactory()));
        this.goRestTemplate = new GoRestTemplate(publicKey, privateKey, restTemplate, timeout);
    }

    public ResponseEntity<List<SystemDeployment>> deploymentsByDate(DeploymentRequest request, Header... additionalHeaders)
            throws GoRestTemplateException {

        ResponseEntity<List<SystemDeployment>> response =
                this.goRestTemplate.get(
                        getUriBuilder(this.rootUrl + DEPLOYMENTS_BY_DATE_URL).build().encode().toUri(), new ParameterizedTypeReference<>() {
                        }, additionalHeaders);

        return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatusCode());
    }

    public ResponseEntity<List<SystemDeployment>> systemsDeploymentByPostDeploymentStatus(DeploymentRequest request, Header... additionalHeaders)
            throws GoRestTemplateException {

        ResponseEntity<List<SystemDeployment>> response =
                this.goRestTemplate.get(
                        getUriBuilder(this.rootUrl + DEPLOYMENTS_BY_POST_DEPLOYMENT_STATUS_URL).build().encode().toUri(), new ParameterizedTypeReference<>() {
                        }, additionalHeaders);

        return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatusCode());
    }

    public ResponseEntity<Integer> deploymentTotalDurationToDeployByDate (DeploymentRequest request, Header... additionalHeaders)
            throws GoRestTemplateException {

        ResponseEntity<Integer> response =
                this.goRestTemplate.get(
                        getUriBuilder(this.rootUrl + DEPLOYMENTS_TOTAL_DURATION_URL).build().encode().toUri(), new ParameterizedTypeReference<>() {
                        }, additionalHeaders);

        return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatusCode());
    }

    public ResponseEntity<Integer> deploymentsBySystem (DeploymentRequest request, Header... additionalHeaders)
            throws GoRestTemplateException {

        ResponseEntity<Integer> response =
                this.goRestTemplate.get(
                        getUriBuilder(this.rootUrl + DEPLOYMENTS_BY_SYSTEM_URL).build().encode().toUri(), new ParameterizedTypeReference<>() {
                        }, additionalHeaders);

        return new ResponseEntity<>(response.getBody(), response.getHeaders(), response.getStatusCode());
    }

    private UriComponentsBuilder getUriBuilder(String url) {
        URI uri = this.goRestTemplate.buildUri(url);
        return UriComponentsBuilder.fromUri(uri);
    }

}
