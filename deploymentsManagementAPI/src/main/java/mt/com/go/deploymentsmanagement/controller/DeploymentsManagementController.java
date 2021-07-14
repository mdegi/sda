package mt.com.go.deploymentsmanagement.controller;

import mt.com.go.deploymentsmanagement.config.AppConfig;
import mt.com.go.deploymentsmanagement.schedulingTasks.FolderScanScheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;

import static mt.com.go.deploymentsmanagement.config.ControllerConstants.*;

@RefreshScope
@RestController
@Validated
public class DeploymentsManagementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeploymentsManagementController.class);

    private final AppConfig appConfig;

    private FolderScanScheduler scheduler;

    private final DeploymentsManagementControllerHelper deploymentsManagementControllerHelper;

    private RequestProcessor requestProcessor;

    public DeploymentsManagementController(AppConfig appConfig, FolderScanScheduler scheduler, RequestProcessor requestProcessor) {
        this.appConfig = appConfig;
        this.scheduler = scheduler;
        this.deploymentsManagementControllerHelper = new DeploymentsManagementControllerHelper();
        this.requestProcessor = requestProcessor;
    }

    @RequestMapping(value = SERVICE_SYSTEMS_DEPLOYMENTS_BY_DATE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getSystemsDeploymentByDate(@PathVariable(PATH_VAR_DEPLOYMENT_DATE) @Pattern(regexp=MAPPING_DATE_REGEX) String deploymentDate) {
        LOGGER.info("Received GET request for getSystemsDeploymentByDate for date: " + deploymentDate);
        return requestProcessor.getSystemsDeploymentByDate(deploymentDate);
    }

    @RequestMapping(value = SERVICE_SYSTEMS_DEPLOYMENTS_BY_STATUS,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getSystemsDeploymentByPostDeploymentStatus(@PathVariable String deploymentStatus) {
        LOGGER.info("Received GET request for getSystemsDeploymentByPostDeploymentStatus for status: " + deploymentStatus);
        return requestProcessor.getSystemsDeploymentByPostDeploymentStatus(deploymentStatus);
    }

    @RequestMapping(value = SERVICE_SYSTEMS_DEPLOYMENT_DURATION_BY_DATE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getDeploymentTotalDurationToDeployByDate(@PathVariable(PATH_VAR_DEPLOYMENT_DATE)
                                                                      @Pattern(regexp=MAPPING_DATE_REGEX) String deploymentDate) {
        LOGGER.info("Received GET request for getDeploymentTotalDurationToDeployByDate for deploymentDate: " + deploymentDate);
        return requestProcessor.getDeploymentTotalDurationToDeployByDate(deploymentDate);
    }

    @RequestMapping(value = SERVICE_SYSTEMS_DEPLOYMENT_WITHIN_DATE_RANGE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getAllSystemDeploymentsWithinDateRange(@PathVariable(PATH_VAR_DATE_FROM) @Pattern(regexp = MAPPING_DATE_REGEX) String dateFrom,
                                                                    @PathVariable(PATH_VAR_DATE_TO) @Pattern(regexp = MAPPING_DATE_REGEX) String dateTo) {
        LOGGER.info("Received GET request for getAllSystemDeploymentsWithinDateRange for deploymentDateRange: " + dateFrom + " - " + dateTo);
        return requestProcessor.getAllSystemDeploymentsWithinDateRange(dateFrom, dateTo);
    }

    @RequestMapping(value = SERVICE_DEPLOYMENTS_BY_SYSTEM,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getAllDeploymentsBySystem(@PathVariable String systemName) {
        LOGGER.info("Received GET request for getAllDeploymentsBySystem for systemName: " + systemName);
        return  requestProcessor.getAllDeploymentsBySystem(systemName);
    }

    @RequestMapping(value = SERVICE_RELOAD_FILES,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.POST)
    public ResponseEntity<?> reloadFiles() {
        LOGGER.info("Received POST request for reloadFiles");
        scheduler.processFileChangesIfAnyREST();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
