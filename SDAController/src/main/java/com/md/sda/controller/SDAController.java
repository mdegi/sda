package com.md.sda.controller;

import com.md.sda.config.AppConfig;
import com.md.sda.model.SystemDeployment;
import com.md.sda.schedulingTasks.FolderScanScheduler;
import com.md.sda.service.SystemDeploymentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Pattern;
import java.util.List;

import static com.md.sda.config.ControllerConstants.*;

@RefreshScope
@RestController
@Validated
public class SDAController implements CommandLineRunner {

    private MongoTemplate mongoTemplate;

    private final AppConfig appConfig;
    private final SystemDeploymentService systemDeploymentService;

    private SDAControllerHelper sdaControllerHelper;

    public SDAController(AppConfig appConfig, SystemDeploymentService systemDeploymentService) {
        this.appConfig = appConfig;
        this.systemDeploymentService = systemDeploymentService;

        sdaControllerHelper = new SDAControllerHelper();
    }

    @RequestMapping(value = V1_SERVICE_CONFIG_VARS_MAPPING,
            method = RequestMethod.GET)
    public String getConfigVarsMapping() {
        return "<html>Configured Vars:" +
                "<br/>fileSystemPath : " + appConfig.getFileSystemPath() +
                "<br/>fileExtension : " + appConfig.getFileExtension() +
                "<br/>fileScanFixedRateMilliSeconds: " + appConfig.getFileScanFixedRateMilliSeconds() +
                "<br/>fileScanInitialDelayMilliSeconds: " + appConfig.getFileScanInitialDelayMilliSeconds() +
                "<br/>mongDBConnectDatabase: " + appConfig.getDbName() +
                "<br/>mongoDBURL: " + appConfig.getDbURL() +
                "</html>";
    }

    @RequestMapping(value = V1_SERVICE_SYSTEMS_DEPLOYMENTS_BY_DATE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getSystemsDeploymentByDate(@PathVariable(PATH_VAR_DEPLOYMENT_DATE) @Pattern(regexp=MAPPING_DATE_REGEX) String deploymentDate) {
        return new ResponseEntity<>(systemDeploymentService.getDeploymentsByDate(sdaControllerHelper.getDate(deploymentDate)), HttpStatus.OK);
    }

    @RequestMapping(value = V1_SERVICE_SYSTEMS_DEPLOYMENTS_BY_STATUS,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getSystemsDeploymentByPostDeploymentStatus(@PathVariable String deploymentStatus) {
        if (deploymentStatus == null  || deploymentStatus.equals("")) {
            return new ResponseEntity<>("Invalid deployment status value", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(systemDeploymentService.getDeploymentsWithPostDeploymentStatus(deploymentStatus), HttpStatus.OK);
        }
    }

    @RequestMapping(value = V1_SERVICE_SYSTEMS_DEPLOYMENT_DURATION_BY_DATE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getDeploymentTotalDurationToDeployByDate(@PathVariable(PATH_VAR_DEPLOYMENT_DATE)
                                                                      @Pattern(regexp=MAPPING_DATE_REGEX) String deploymentDate) {
        List<SystemDeployment> deploymentList = systemDeploymentService.getDeploymentsByDate(sdaControllerHelper.getDate(deploymentDate));
        if (!deploymentList.isEmpty()) {
            return  new ResponseEntity<>(deploymentList.stream()
                                            .map(SystemDeployment::getActualProdDeploymentDurationMinutes)
                                            .reduce(0, Integer::sum), HttpStatus.OK);
        }
        return new ResponseEntity<>(0, HttpStatus.OK);
    }

    @RequestMapping(value = V1_SERVICE_SYSTEMS_DEPLOYMENT_WITHIN_DATE_RANGE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getAllSystemDeploymentsWithinDateTimeRange(@PathVariable(PATH_VAR_DATE_FROM) @Pattern(regexp = MAPPING_DATE_REGEX) String dateFrom,
                                                                        @PathVariable(PATH_VAR_DATE_TO) @Pattern(regexp = MAPPING_DATE_REGEX) String dateTo) {
            return new ResponseEntity<>(systemDeploymentService.getDeploymentsByDateRange(sdaControllerHelper.getDate(dateFrom), sdaControllerHelper.getDate(dateTo)), HttpStatus.OK);
    }

    @RequestMapping(value = V1_SERVICE_DEPLOYMENTS_BY_SYSTEM,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getAllDeploymentsBySystem(@PathVariable String systemName) {
        if (systemName == null  || systemName.equals("")) {
            return new ResponseEntity<>("System cannot be left blank in request", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(systemDeploymentService.getDeploymentsBySystem(systemName), HttpStatus.OK);
        }
    }

    @RequestMapping(value = V1_SERVICE_RELOAD_FILES,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.POST)
    public ResponseEntity<?> reloadFiles() {
        FolderScanScheduler scheduler = new FolderScanScheduler(appConfig, systemDeploymentService, mongoTemplate);
        scheduler.processFileChangesIfAnyREST();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public void run(String... args) {
        if (systemDeploymentService.getMongoTemplate() == null) {
            systemDeploymentService.setMongoTemplate(mongoTemplate);
        }
    }


}
