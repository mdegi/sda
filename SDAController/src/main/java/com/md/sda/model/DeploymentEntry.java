package com.md.sda.model;

import com.md.sda.objects.OSFile;

import java.util.List;

public interface DeploymentEntry {

    public int getLineNumber();
    public String getSponsor();
    public String getStatus();
    public String getStagingStatus();
    public String getSystemName();
    public String getProjectInitiative();
    public String getDeploymentInstructions();
    public String getDependencies();
    public String getReleaseNotes();
    public String getContactPerson();
    public String getPeerReviewer();
    public String getActualSTGDeploymentDurationMinutes();
    public int getProjectedDurationMinutes();
    public int getActualProdDeploymentDurationMinutes();
    public String getCanBeDoneDuringTheDay();
    public String getDeploymentApplicationDate();
    public String getDeploymentAutomation();
    public String getDevPostDeploymentTasks();

    public List<DeploymentEntry> getDeploymentEntries(OSFile osFile);

}
