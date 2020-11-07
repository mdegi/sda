package com.md.sda.controller;

import com.md.sda.config.AppConfig;
import com.md.sda.model.SystemDeployment;
import com.md.sda.service.SystemDeploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.md.sda.config.ControllerConstants.CONFIG_VARS_MAPPING;
import static com.md.sda.config.ControllerConstants.FILE_SYSTEM_PATH_MAPPING;

@RefreshScope
@RestController
public class SDAController implements CommandLineRunner {

    @Autowired
    MongoTemplate mongoTemplate;

    private final AppConfig appConfig;
    private final SystemDeploymentService systemDeploymentService;

    public SDAController(AppConfig appConfig, SystemDeploymentService systemDeploymentService) {
        this.appConfig = appConfig;
        this.systemDeploymentService = systemDeploymentService;
    }

    @RequestMapping(value = CONFIG_VARS_MAPPING,
            method = RequestMethod.GET)
    String getConfigVarsMapping() {
        return "Configured Vars:" +
                "<br/>fileSystemPath : " + appConfig.getFileSystemPath() +
                "<br/>fileScanFixedRateMilliSeconds: " + appConfig.getFileScanFixedRateMilliSeconds() +
                "<br/>fileScanInitialDelayMilliSeconds: " + appConfig.getFileScanInitialDelayMilliSeconds();
    }

    @RequestMapping(value = FILE_SYSTEM_PATH_MAPPING,
            method = RequestMethod.GET)
    String getFileSystemPath() {
        return "Configured File System Path is: " + appConfig.getFileSystemPath();
    }

    @Override
    public void run(String... args) {
        systemDeploymentService.setMongoTemplate(mongoTemplate);
        systemDeploymentService.insertRecord(new SystemDeployment("PSSP","NS","MD","IP"));
    }


}
