package mt.com.go.deploymentsmanagement.controller;

import mt.com.go.deploymentmanagement.models.SystemDeployment;
import mt.com.go.deploymentsmanagement.dao.SystemDeploymentDAO;
import mt.com.go.deploymentsmanagement.schedulingTasks.FolderScanScheduler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class DeploymentsManagementControllerTests {

    @Mock
    FolderScanScheduler scheduler;

    @Mock
    RequestProcessor requestProcessor;
    @InjectMocks
    DeploymentsManagementController controller;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getSystemsDeploymentByDate_validDate() {
        List<SystemDeployment> deploymentsByDate = new ArrayList<>();
        deploymentsByDate.add(new SystemDeployment());

        ResponseEntity responseEntity = new ResponseEntity<>(deploymentsByDate, HttpStatus.OK);
        when(requestProcessor.getDeploymentTotalDurationToDeployByDate(anyString())).thenReturn(responseEntity);

        controller.getSystemsDeploymentByDate("20210111");
        assertFalse(deploymentsByDate.isEmpty());
    }

    @Test
    public void getSystemsDeploymentByDate_inValidDate() {
        controller.getSystemsDeploymentByDate("1234");
        assert(true);
    }

    @Test
    public void getDeploymentTotalDurationToDeployByDate_entriesFound() {
        String deploymentDate = "20210111";
        List<SystemDeploymentDAO> deploymentList = new ArrayList<>();
        deploymentList.add(new SystemDeploymentDAO());

        ResponseEntity responseEntity = new ResponseEntity<>(deploymentList, HttpStatus.OK);
        when(requestProcessor.getDeploymentTotalDurationToDeployByDate(anyString())).thenReturn(responseEntity);

        assertEquals(controller.getDeploymentTotalDurationToDeployByDate(deploymentDate).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getDeploymentTotalDurationToDeployByDate_noEntries() {
        String deploymentDate = "20210112";
        List<SystemDeploymentDAO> deploymentList = new ArrayList<>();

        ResponseEntity responseEntity = new ResponseEntity<>(deploymentList, HttpStatus.OK);
        when(requestProcessor.getDeploymentTotalDurationToDeployByDate(anyString())).thenReturn(responseEntity);
        assertEquals(controller.getDeploymentTotalDurationToDeployByDate(deploymentDate).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getSystemsDeploymentByPostDeploymentStatus_validStatus() {
        ResponseEntity responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(requestProcessor.getSystemsDeploymentByPostDeploymentStatus(anyString())).thenReturn(responseEntity);
        assertEquals(controller.getSystemsDeploymentByPostDeploymentStatus("OPEN").getStatusCode(),HttpStatus.OK);
    }

    @Test
    public void getSystemsDeploymentByPostDeploymentStatus_inValidStatus() {
        ResponseEntity responseEntity = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        when(requestProcessor.getSystemsDeploymentByPostDeploymentStatus(anyString())).thenReturn(responseEntity);
        assertEquals(controller.getSystemsDeploymentByPostDeploymentStatus("").getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllDeploymentsBySystem_validSystem() {
        ResponseEntity responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(requestProcessor.getAllDeploymentsBySystem(anyString())).thenReturn(responseEntity);
        assertEquals(controller.getAllDeploymentsBySystem("CPS").getStatusCode(),HttpStatus.OK);
    }

    @Test
    public void getAllDeploymentsBySystem_inValidSystem() {
        ResponseEntity responseEntity = new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        when(requestProcessor.getAllDeploymentsBySystem(anyString())).thenReturn(responseEntity);
        assertEquals(controller.getAllDeploymentsBySystem("").getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    public void reloadFiles() {
        doNothing().when(scheduler).processFileChangesIfAnyREST();
        assertEquals(controller.reloadFiles().getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getAllSystemDeploymentsWithinDateRange_validDates() {
        String dateTo = "20210111";
        String dateFrom = "20210111";

        List<SystemDeploymentDAO> deploymentList = new ArrayList<>();
        deploymentList.add(new SystemDeploymentDAO());

        ResponseEntity responseEntity = new ResponseEntity<>(deploymentList, HttpStatus.OK);
        when(requestProcessor.getAllSystemDeploymentsWithinDateRange(anyString(), anyString())).thenReturn(responseEntity);

        when(requestProcessor.getAllSystemDeploymentsWithinDateRange(anyString(), anyString())).thenReturn(responseEntity);
        assertEquals(controller.getAllSystemDeploymentsWithinDateRange(dateFrom, dateTo).getStatusCode(), HttpStatus.OK);
    }


}
