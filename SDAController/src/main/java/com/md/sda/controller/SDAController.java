package com.md.sda.controller;

import com.md.sda.config.AppConfig;
import com.md.sda.model.SystemDeployment;
import com.md.sda.service.SystemDeploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

import static com.md.sda.config.ControllerConstants.*;

@RefreshScope
@RestController
public class SDAController implements CommandLineRunner {

    @Autowired
    MongoTemplate mongoTemplate;

    private final AppConfig appConfig;
    private final SystemDeploymentService systemDeploymentService;

    private SDAControllerHelper sdaControllerHelper;

    public SDAController(AppConfig appConfig, SystemDeploymentService systemDeploymentService) {
        this.appConfig = appConfig;
        this.systemDeploymentService = systemDeploymentService;

        sdaControllerHelper = new SDAControllerHelper(systemDeploymentService);
    }

    @RequestMapping(value = V1_SERVICE_CONFIG_VARS_MAPPING,
            method = RequestMethod.GET)
    public String getConfigVarsMapping() {
        return "Configured Vars:" +
                "<br/>fileSystemPath : " + appConfig.getFileSystemPath() +
                "<br/>fileScanFixedRateMilliSeconds: " + appConfig.getFileScanFixedRateMilliSeconds() +
                "<br/>fileScanInitialDelayMilliSeconds: " + appConfig.getFileScanInitialDelayMilliSeconds() +
                "<br/>mongDBConectDatabase: " + appConfig.getDbName() +
                "<br/>mongoDBURL: " + appConfig.getDbURL();
    }

    @RequestMapping(value = V1_SERVICE_SYSTEMS_DEPLOYMENTS_BY_DATE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getSystemsByDeploymentByDate(@PathVariable String deploymentDate) {
        if (isValidDate(deploymentDate)) {
            return new ResponseEntity<>("Date parameter not in correct format", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new SystemDeployment(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = V1_SERVICE_SYSTEMS_DEPLOYMENTS_BY_STATUS,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getSystemsByDeploymentByStatus(@PathVariable String deploymentStatus) {

        if (deploymentStatus == null  || deploymentStatus.equals("") || Arrays.stream(DeploymentStatus.values()).noneMatch(d -> d.toString().equals(deploymentStatus))) {
            return new ResponseEntity<>("Invalid deployment status value", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new SystemDeployment(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = V1_SERVICE_SYSTEMS_DEPLOYMENT_DURATION_BY_DATE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getDeploymentTotalDurationToDeployByDate(@PathVariable String deploymentDate) {
        if (isValidDate(deploymentDate)) {
            return new ResponseEntity<>("Date parameter not in correct format", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new SystemDeployment(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = V1_SERVICE_SYSTEMS_DEPLOYMENT_WITHIN_TIME_RANGE,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getAllSystemDeploymentsWithinDateTimeRange(@PathVariable String timeFrom,
                                                                        @PathVariable String timeTo) {
        if (!sdaControllerHelper.isValidTime(timeFrom) || !sdaControllerHelper.isValidTime(timeTo)) {
            return new ResponseEntity<>("Time parameter(s) not in correct format", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new SystemDeployment(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = V1_SERVICE_DEPLOYMENTS_BY_SYSTEM,
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE},
            method = RequestMethod.GET)
    public ResponseEntity<?> getAllDeploymentsBySystem(@PathVariable String systemName) {
        if (systemName == null  || systemName.equals("")) {
            return new ResponseEntity<>("System cannot be left blank in request", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(new SystemDeployment(), HttpStatus.OK);
        }
    }

    private boolean isValidDate(String deploymentDate) {
        return (deploymentDate == null  || deploymentDate.equals("") || !sdaControllerHelper.isValidDate(deploymentDate));
    }

    @Override
    public void run(String... args) {
        if (systemDeploymentService.getMongoTemplate() == null) {
            systemDeploymentService.setMongoTemplate(mongoTemplate);
        }
    }


}
