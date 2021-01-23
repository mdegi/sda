package mt.com.go.deploymentsmanagement.model;

import mt.com.go.deploymentsmanagement.objects.OSFile;

import java.util.List;

public interface DeploymentEntry {

    int getLineNumber();
    String getSponsor();
    String getStatus();
    String getStagingStatus();
    String getSystemName();
    String getProjectInitiative();
    String getDeploymentInstructions();
    String getDependencies();
    String getReleaseNotes();
    String getContactPerson();
    String getPeerReviewer();
    String getActualSTGDeploymentDurationMinutes();
    int getProjectedDurationMinutes();
    int getActualProdDeploymentDurationMinutes();
    String getCanBeDoneDuringTheDay();
    String getDeploymentApplicationDate();
    String getDeploymentAutomation();
    String getDevPostDeploymentTasks();

    List<DeploymentEntry> getDeploymentEntries(OSFile osFile);

}
