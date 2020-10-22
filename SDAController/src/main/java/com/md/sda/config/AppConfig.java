package com.md.sda.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("appConfig")
public class AppConfig {

    @Value("${fileSystemPath:defaultPath}")
    private String fileSystemPath;

    public String getFileSystemPath() {
        return fileSystemPath;
    }
}
