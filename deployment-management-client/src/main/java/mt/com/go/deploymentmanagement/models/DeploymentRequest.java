package mt.com.go.deploymentmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeploymentRequest extends BaseRequest{

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

}
