package com.md.sda.controller;

import com.md.sda.config.AppConfig;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static  com.md.sda.config.ControllerConstants.*;


@RefreshScope
@RestController
public class SDAController {

    private final AppConfig appConfig;

    public SDAController(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @RequestMapping(value = FILE_SYSTEM_PATH_MAPPING,
            method = RequestMethod.GET)
    String getFileSystemPath() {
        return "Configured File System Path is: " + appConfig.getFileSystemPath();
    }

}
