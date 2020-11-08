package com.md.sda.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AppSystem {

    private String sponsor;
    private String systemName;
    private String status;
    private String projectInitiative;
    private String deploymentInstructions;

    private List<String> dependencies;

    private String releaseNotes;

    private List<String> contactPerson;
    private List<String> peerReviewers;

    private String decPostDeploymentJiraRef;

    private int actualDeploymentDurationStg;
    private int projectedDuration;
    private int actualDeploymentDurationProd;

    private boolean canBeDoneDuringTheDay;

    private Date deploymentApplicationDate;

    private String deploymentAutomatiom;


}
