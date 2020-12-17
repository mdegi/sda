package com.md.sda.controller;

import com.md.sda.schedulingTasks.FolderScanScheduler;
import com.md.sda.service.SystemDeploymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

@Component
public class SDAControllerHelper {

    private final SystemDeploymentService systemDeploymentService;

    public SDAControllerHelper(SystemDeploymentService systemDeploymentService) {
        this.systemDeploymentService = systemDeploymentService;
    }

    public boolean isValidDate(String sqlFormatDate) {
        boolean validDate = false;

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(sqlFormatDate);
            validDate = true;
        } catch (ParseException e) {
            // date is not parsed
        }
        return validDate;
    }

    public boolean isValidTime(String timeString) {
        boolean validTime = false;
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        try {
            TemporalAccessor time = timeFormat.parse(timeString);
            validTime = true;
        } catch (DateTimeParseException e) {
            // time is not parsed
        }
        return validTime;
    }

}
