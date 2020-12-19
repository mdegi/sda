package com.md.sda.model;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CSVDeploymentEntry {

    @CsvBindByName(column = "#")
    private int lineNumber;

    @CsvBindByName (column = "Sponsor")
    private String sponsor;

    @CsvBindByName (column = "Status")
    private String status;

    @CsvBindByName (column = "Deployed to staging server?")
    private String stagingStatus;

    @CsvBindByName (column = "System/Component to be deployed")
    private String systemName;

    @CsvBindByName (column = "Project/Initiative")
    private String projectInitiative;

    @CsvBindByName (column = "Deployment Instructions(and post-deployment tests)")
    private String deploymentInstructions;

    @CsvBindByName (column = "Dependencies on other deployments")
    private String dependencies;

    @CsvBindByName (column = "Release Notes")
    private String releaseNotes;

    @CsvBindByName (column = "Contact Person")
    private String contactPerson;

    @CsvBindByName (column = "Peer Reviewer")
    private String peerReviewer;

    @CsvBindByName (column = "Actual Deployment Duration on Staging(Minutes)")
    private String actualSTGDeploymentDurationMinutes;

    @CsvBindByName (column = "Projected Deployment Duration on Production(Minutes)")
    private int projectedDurationMinutes;

    @CsvBindByName (column = "Actual Deployment Duration on Production(Minutes)")
    private int actualProdDeploymentDurationMinutes;

    @CsvBindByName (column = "Can be done during the day? (Yes/No)")
    private String canBeDoneDuringTheDay;

    @CsvBindByName (column = "Deployment Application Date")
    private String deploymentApplicationDate;

    @CsvBindByName (column = "Deployment Automation (Full/Partial/None)")
    private String deploymentAutomation;

    @CsvBindByName (column = "Dev post-deployment Jira tasks (see here)")
    private String devPostDeploymentTasks;

}
