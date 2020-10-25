package com.md.sda.schedulingTasks;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ReadFolderScheduler {

    private static final Logger log = LoggerFactory.getLogger(ReadFolderScheduler.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    //a scheduled method should have the void return type
    //a method should not accept any parameters
    //options are fixedRate and fixedDelay
    @Scheduled(fixedRateString = "${fileScanFixedRateMilliSeconds}")
    public void reportCurrentTime() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
