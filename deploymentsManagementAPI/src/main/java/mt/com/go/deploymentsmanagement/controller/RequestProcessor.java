package mt.com.go.deploymentsmanagement.controller;

import mt.com.go.deploymentmanagement.models.SystemDeployment;
import mt.com.go.deploymentsmanagement.dao.SystemDeploymentDAO;
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
        List<SystemDeploymentDAO> systemDeploymentDAOS = systemDeploymentService.getDeploymentsByDate(deploymentsManagementControllerHelper.getDate(deploymentDate));
        return new ResponseEntity<>(systemDeploymentDAOS.stream().map(this::convertSystemDeployment).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<?> getDeploymentTotalDurationToDeployByDate(String deploymentDate) {
        LOGGER.info("Processing getDeploymentTotalDurationToDeployByDate: " + deploymentDate);
        List<SystemDeploymentDAO> deploymentList = systemDeploymentService.getDeploymentsByDate(deploymentsManagementControllerHelper.getDate(deploymentDate));
        if (!deploymentList.isEmpty()) {
            return  new ResponseEntity<>(deploymentList.stream()
                    .map(SystemDeploymentDAO::getActualProdDeploymentDurationMinutes)
                    .reduce(0, Integer::sum), HttpStatus.OK);
        }
        return new ResponseEntity<>(0, HttpStatus.OK);
    }

    public ResponseEntity<?> getSystemsDeploymentByPostDeploymentStatus(String deploymentStatus) {
        LOGGER.info("Processing getSystemsDeploymentByPostDeploymentStatus: " + deploymentStatus);
        if (deploymentStatus == null  || deploymentStatus.equals("")) {
            return new ResponseEntity<>("Invalid deployment status value", HttpStatus.BAD_REQUEST);
        } else {
            List<SystemDeploymentDAO> systemDeploymentDAOS = systemDeploymentService.getDeploymentsWithPostDeploymentStatus(deploymentStatus);
            return new ResponseEntity<>(systemDeploymentDAOS.stream().map(this::convertSystemDeployment).collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    public ResponseEntity<?> getAllSystemDeploymentsWithinDateRange(String dateFrom, String dateTo) {
        LOGGER.info("Processing getAllSystemDeploymentsWithinDateRange: " + dateFrom + " - " + dateTo);
        List<SystemDeploymentDAO> deploymentList = systemDeploymentService.getDeploymentsByDateRange(deploymentsManagementControllerHelper.getDate(dateFrom), deploymentsManagementControllerHelper.getDate(dateTo));
        return new ResponseEntity<>(deploymentList.stream().map(this::convertSystemDeployment).collect(Collectors.toList()), HttpStatus.OK);
    }

    public ResponseEntity<?> getAllDeploymentsBySystem(String systemName) {
        LOGGER.info("Processing getAllDeploymentsBySystem: " + systemName);
        if (systemName == null  || systemName.equals("")) {
            return new ResponseEntity<>("System cannot be left blank in request", HttpStatus.BAD_REQUEST);
        } else {
            List<SystemDeploymentDAO> deploymentList = systemDeploymentService.getDeploymentsBySystem(systemName);
            return new ResponseEntity<>(deploymentList.stream().map(this::convertSystemDeployment).collect(Collectors.toList()), HttpStatus.OK);
        }
    }

    private SystemDeployment convertSystemDeployment(SystemDeploymentDAO systemDeploymentDAO) {
        LOGGER.info("Converting entity object to systemDeployment object : " + systemDeploymentDAO.getDeploymentDate() + " - " + systemDeploymentDAO.getSystemName());
        mt.com.go.deploymentmanagement.models.SystemDeployment systemDeployment = new mt.com.go.deploymentmanagement.models.SystemDeployment();
        systemDeployment.setId(systemDeploymentDAO.getId());
        systemDeployment.setLineNumber(systemDeploymentDAO.getLineNumber());
        systemDeployment.setSponsor(systemDeploymentDAO.getSponsor());
        systemDeployment.setStatus(systemDeploymentDAO.getStatus());
        systemDeployment.setStagingStatus(systemDeploymentDAO.getStagingStatus());
        systemDeployment.setDeploymentDate(systemDeploymentDAO.getDeploymentDate());
        systemDeployment.setDeploymentAutomation(systemDeploymentDAO.getDeploymentAutomation());
        systemDeployment.setSystemName(systemDeploymentDAO.getSystemName());
        systemDeployment.setProjectInitiative(systemDeploymentDAO.getProjectInitiative());
        systemDeployment.setDeploymentInstructions(systemDeploymentDAO.getDeploymentInstructions());
        systemDeployment.setDependencies(systemDeploymentDAO.getDependencies());
        systemDeployment.setReleaseNotes(systemDeploymentDAO.getReleaseNotes());
        systemDeployment.setContactPerson(systemDeploymentDAO.getContactPerson());
        systemDeployment.setPeerReviewer(systemDeploymentDAO.getPeerReviewer());
        systemDeployment.setActualProdDeploymentDurationMinutes(systemDeploymentDAO.getActualProdDeploymentDurationMinutes());
        systemDeployment.setProjectedDurationMinutes(systemDeploymentDAO.getProjectedDurationMinutes());
        systemDeployment.setActualProdDeploymentDurationMinutes(systemDeploymentDAO.getActualProdDeploymentDurationMinutes());
        systemDeployment.setCanBeDoneDuringTheDay(systemDeploymentDAO.getCanBeDoneDuringTheDay());
        systemDeployment.setDeploymentApplicationDate(systemDeploymentDAO.getDeploymentApplicationDate());
        systemDeployment.setDeploymentAutomation(systemDeploymentDAO.getDeploymentAutomation());
        systemDeployment.setDevPostDeploymentTasks(systemDeploymentDAO.getDevPostDeploymentTasks());
        systemDeployment.setDeploymentDate(systemDeploymentDAO.getDeploymentDate());

        return systemDeployment;
    }

}
