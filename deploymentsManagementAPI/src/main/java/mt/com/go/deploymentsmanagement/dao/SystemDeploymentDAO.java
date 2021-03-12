package mt.com.go.deploymentsmanagement.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Getter
@Setter
@NoArgsConstructor
public class SystemDeploymentDAO {

    @Id
    public String id;

    private int lineNumber;
    private String sponsor;
    private String status;
    private String stagingStatus;
    private String systemName;
    private String projectInitiative;
    private String deploymentInstructions;
    private String dependencies;
    private String releaseNotes;
    private String contactPerson;
    private String peerReviewer;
    private String actualSTGDeploymentDurationMinutes;
    private int projectedDurationMinutes;
    private int actualProdDeploymentDurationMinutes;
    private String canBeDoneDuringTheDay;
    private String deploymentApplicationDate;
    private String deploymentAutomation;
    private String devPostDeploymentTasks;
    private Date deploymentDate;

    @Override
    public String toString() {
        return String.format(
                "SystemDeployment[id=%s, lineNumber='%s', sponsor='%s', " +
                        "status,=%s stagingStatus=%s," +
                        "systemName=%s, projectInitiative=%s," +
                        "deploymentInstructions=%s, dependencies=%s," +
                        "releaseNotes=%s, contactPerson=%s," +
                        "peerReviewer=%s, actualSTGDeploymentDurationMinutes=%s," +
                        "projectedDurationMinutes=%s, actualProdDeploymentDurationMinutes=%s," +
                        "canBeDoneDuringTheDay=%s, deploymentApplicationDate=%s," +
                        "deploymentAutomation=%s, devPostDeploymentTasks='%sm deploymentDate=%s']",
                id, lineNumber, sponsor, status,  stagingStatus, systemName, projectInitiative,
                deploymentInstructions, dependencies, releaseNotes, contactPerson, peerReviewer, actualSTGDeploymentDurationMinutes,
                projectedDurationMinutes, actualProdDeploymentDurationMinutes, canBeDoneDuringTheDay, deploymentApplicationDate,
                deploymentAutomation, devPostDeploymentTasks, deploymentDate);
    }

}