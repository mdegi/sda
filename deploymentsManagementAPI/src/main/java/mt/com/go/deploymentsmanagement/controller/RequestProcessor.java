package mt.com.go.deploymentsmanagement.controller;

import mt.com.go.deploymentmanagement.models.SystemDeployment;
import mt.com.go.deploymentsmanagement.dao.SystemDeploymentRepo;
import mt.com.go.deploymentsmanagement.service.SystemDeploymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessor.class);

    private final SystemDeploymentService systemDeploymentService;

    private final DeploymentsManagementControllerHelper deploymentsManagementControllerHelper;

    public RequestProcessor(SystemDeploymentService systemDeploymentService, DeploymentsManagementControllerHelper deploymentsManagementControllerHelper) {
        this.systemDeploymentService = systemDeploymentService;
        this.deploymentsManagementControllerHelper = deploymentsManagementControllerHelper;
    }

    public ResponseEntity<?> getSystemsDeploymentByDate(String deploymentDate) {
        LOGGER.info("Processing getSystemsDeploymentByDate: " + deploymentDate);
        List<SystemDeploymentRepo> systemDeploymentRepos = systemDeploymentService.getDeploymentsByDate(deploymentsManagementControllerHelper.getDate(deploymentDate));
        return new ResponseEntity<>(systemDeploymentRepos.stream().map(this::convertSystemDeployment).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<?> getDeploymentTotalDurationToDeployByDate(String deploymentDate) {
        LOGGER.info("Processing getDeploymentTotalDurationToDeployByDate: " + deploymentDate);
        List<SystemDeploymentRepo> deploymentList = systemDeploymentService.getDeploymentsByDate(deploymentsManagementControllerHelper.getDate(deploymentDate));
        if (!deploymentList.isEmpty()) {
            return  new ResponseEntity<>(deploymentList.stream()
                    .map(SystemDeploymentRepo::getActualProdDeploymentDurationMinutes)
                    .reduce(0, Integer::sum), HttpStatus.OK);
        }
        return new ResponseEntity<>(0, HttpStatus.OK);
    }

    public ResponseEntity<?> getSystemsDeploymentByPostDeploymentStatus(String deploymentStatus) {
        LOGGER.info("Processing getSystemsDeploymentByPostDeploymentStatus: " + deploymentStatus);
        if (deploymentStatus == null  || deploymentStatus.equals("")) {
            return new ResponseEntity<>("Invalid deployment status value", HttpStatus.BAD_REQUEST);
        } else {
            List<SystemDeploymentRepo> systemDeploymentRepos = systemDeploymentService.getDeploymentsWithPostDeploymentStatus(deploymentStatus);
            return new ResponseEntity<>(systemDeploymentRepos.stream().map(this::convertSystemDeployment).collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> getAllSystemDeploymentsWithinDateRange(String dateFrom, String dateTo) {
        LOGGER.info("Processing getAllSystemDeploymentsWithinDateRange: " + dateFrom + " - " + dateTo);
        List<SystemDeploymentRepo> deploymentList = systemDeploymentService.getDeploymentsByDateRange(deploymentsManagementControllerHelper.getDate(dateFrom), deploymentsManagementControllerHelper.getDate(dateTo));
        return new ResponseEntity<>(deploymentList.stream().map(this::convertSystemDeployment).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<?> getAllDeploymentsBySystem(String systemName) {
        LOGGER.info("Processing getAllDeploymentsBySystem: " + systemName);
        if (systemName == null  || systemName.equals("")) {
            return new ResponseEntity<>("System cannot be left blank in request", HttpStatus.BAD_REQUEST);
        } else {
            List<SystemDeploymentRepo> deploymentList = systemDeploymentService.getDeploymentsBySystem(systemName);
            return new ResponseEntity<>(deploymentList.stream().map(this::convertSystemDeployment).collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    private SystemDeployment convertSystemDeployment(SystemDeploymentRepo systemDeploymentRepo) {
        LOGGER.info("Converting entity object to systemDeployment object : " + systemDeploymentRepo.getDeploymentDate() + " - " + systemDeploymentRepo.getSystemName());
        mt.com.go.deploymentmanagement.models.SystemDeployment systemDeployment = new mt.com.go.deploymentmanagement.models.SystemDeployment();
        systemDeployment.setId(systemDeploymentRepo.getId());
        systemDeployment.setLineNumber(systemDeploymentRepo.getLineNumber());
        systemDeployment.setSponsor(systemDeploymentRepo.getSponsor());
        systemDeployment.setStatus(systemDeploymentRepo.getStatus());
        systemDeployment.setStagingStatus(systemDeploymentRepo.getStagingStatus());
        systemDeployment.setDeploymentDate(systemDeploymentRepo.getDeploymentDate());
        systemDeployment.setDeploymentAutomation(systemDeploymentRepo.getDeploymentAutomation());
        systemDeployment.setSystemName(systemDeploymentRepo.getSystemName());
        systemDeployment.setProjectInitiative(systemDeploymentRepo.getProjectInitiative());
        systemDeployment.setDeploymentInstructions(systemDeploymentRepo.getDeploymentInstructions());
        systemDeployment.setDependencies(systemDeploymentRepo.getDependencies());
        systemDeployment.setReleaseNotes(systemDeploymentRepo.getReleaseNotes());
        systemDeployment.setContactPerson(systemDeploymentRepo.getContactPerson());
        systemDeployment.setPeerReviewer(systemDeploymentRepo.getPeerReviewer());
        systemDeployment.setActualProdDeploymentDurationMinutes(systemDeploymentRepo.getActualProdDeploymentDurationMinutes());
        systemDeployment.setProjectedDurationMinutes(systemDeploymentRepo.getProjectedDurationMinutes());
        systemDeployment.setActualProdDeploymentDurationMinutes(systemDeploymentRepo.getActualProdDeploymentDurationMinutes());
        systemDeployment.setCanBeDoneDuringTheDay(systemDeploymentRepo.getCanBeDoneDuringTheDay());
        systemDeployment.setDeploymentApplicationDate(systemDeploymentRepo.getDeploymentApplicationDate());
        systemDeployment.setDeploymentAutomation(systemDeploymentRepo.getDeploymentAutomation());
        systemDeployment.setDevPostDeploymentTasks(systemDeploymentRepo.getDevPostDeploymentTasks());
        systemDeployment.setDeploymentDate(systemDeploymentRepo.getDeploymentDate());

        return systemDeployment;
    }

}
