package com.md.sda.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component("appConfig")
public class AppConfig {

    @Value("${fileSystemPath:defaultPath}")
    private String fileSystemPath;

    @Value("${fileScanFixedRateMilliSeconds}")
    private  Integer fileScanFixedRateMilliSeconds;

    @Value("${fileScanInitialDelayMilliSeconds}")
    private  Integer fileScanInitialDelayMilliSeconds;

}
