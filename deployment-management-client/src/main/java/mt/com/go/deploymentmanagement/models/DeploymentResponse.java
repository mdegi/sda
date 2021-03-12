package mt.com.go.deploymentmanagement.models;

import java.util.ArrayList;

public class DeploymentResponse extends DeploymentRequest {

    public static DeploymentResponse fromRequest(DeploymentRequest request) {
        DeploymentResponse response = new DeploymentResponse();

        response.setId(request.id);

        response.setLineNumber(request.getLineNumber());
        response.setSponsor(request.getSponsor());
        response.setStatus(request.getStatus());
        response.setStagingStatus(request.getStagingStatus());
        response.setSystemName(request.getSystemName());
        response.setProjectInitiative(request.getProjectInitiative());
        response.setDeploymentInstructions(request.getDeploymentInstructions());
        response.setDependencies(request.getDependencies());
        response.setReleaseNotes(request.getReleaseNotes());
        response.setContactPerson(request.getContactPerson());
        response.setPeerReviewer(request.getPeerReviewer());
        response.setActualSTGDeploymentDurationMinutes(request.getActualSTGDeploymentDurationMinutes());
        response.setProjectedDurationMinutes(request.getProjectedDurationMinutes());
        response.setActualProdDeploymentDurationMinutes(request.getActualProdDeploymentDurationMinutes());
        response.setCanBeDoneDuringTheDay(request.getCanBeDoneDuringTheDay());
        response.setDeploymentApplicationDate(request.getDeploymentApplicationDate());
        response.setDeploymentAutomation(request.getDeploymentAutomation());
        response.setDevPostDeploymentTasks(request.getDevPostDeploymentTasks());
        response.setDeploymentDate(request.getDeploymentDate());

        response.setUdfs(new ArrayList<Udf>(request.getUdfs()));
        return response;
    }
}


