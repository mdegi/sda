package mt.com.go.deploymentsmanagement.controller;

import mt.com.go.deploymentsmanagement.config.AppConfig;
import mt.com.go.deploymentsmanagement.model.SystemDeployment;
import mt.com.go.deploymentsmanagement.schedulingTasks.FolderScanScheduler;
import mt.com.go.deploymentsmanagement.service.SystemDeploymentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static mt.com.go.deploymentsmanagement.config.AppConfigConstants.* ;

public class DeploymentsManagementControllerTests {

    @Mock
    FolderScanScheduler scheduler;

    @Mock
    AppConfig appConfig;

    @Mock
    SystemDeploymentService systemDeploymentService;

    @Mock
    DeploymentsManagementControllerHelper controllerHelper;

    @InjectMocks
    DeploymentsManagementController controller;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getConfigVarsMapping_validResponse() {
        when(appConfig.getFileSystemPath()).thenReturn(FILE_SYSTEM_PATH);
        when(appConfig.getFilenameRegex()).thenReturn(FILE_NAME_REGEX);
        when(appConfig.getFileExtension()).thenReturn(FILE_EXTENSION);
        when(appConfig.getFileScanFixedRateMilliSeconds()).thenReturn(FILE_SCAN_FIXED_RATE_MILLISECONDS);
        when(appConfig.getFileScanInitialDelayMilliSeconds()).thenReturn(FILE_SCAN_INITIAL_DELAY_MILLISECONDS);
        when(appConfig.getDbURL()).thenReturn(DB_URL);
        when(appConfig.getDbName()).thenReturn(DB_NAME);

        String response = controller.getConfigVarsMapping();
        assertTrue(response.startsWith("<html>") && response.endsWith("</html>"));
    }

    @Test
    public void getSystemsDeploymentByDate_validDate() {
        List<SystemDeployment> deploymentsByDate = new ArrayList<>();
        deploymentsByDate.add(new SystemDeployment());

        when(controllerHelper.getDate(anyString())).thenReturn(new Date());
        when(systemDeploymentService.getDeploymentsByDate(any())).thenReturn(deploymentsByDate);
        controller.getSystemsDeploymentByDate("20210111");
        assertFalse(deploymentsByDate.isEmpty());
    }

    @Test
    public void getSystemsDeploymentByDate_inValidDate() {
        when(controllerHelper.getDate(anyString())).thenReturn(null);
        when(systemDeploymentService.getDeploymentsByDate(null)).thenReturn(null);

        controller.getSystemsDeploymentByDate("1234");
        assert(true);
    }

    @Test
    public void getDeploymentTotalDurationToDeployByDate_entriesFound() {
        String deploymentDate = "20210111";
        List<SystemDeployment> deploymentList = new ArrayList<>();
        deploymentList.add(new SystemDeployment());

        when(controllerHelper.getDate(anyString())).thenReturn(new Date());
        when(systemDeploymentService.getDeploymentsByDate(any())).thenReturn(deploymentList);

        assertEquals(controller.getDeploymentTotalDurationToDeployByDate(deploymentDate).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getDeploymentTotalDurationToDeployByDate_noEntries() {
        String deploymentDate = "20210112";
        List<SystemDeployment> deploymentList = new ArrayList<>();
        when(systemDeploymentService.getDeploymentsByDate(controllerHelper.getDate(deploymentDate))).thenReturn(deploymentList);

        assertEquals(controller.getDeploymentTotalDurationToDeployByDate(deploymentDate).getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getSystemsDeploymentByPostDeploymentStatus_validStatus() {
        assertEquals(controller.getSystemsDeploymentByPostDeploymentStatus("OPEN").getStatusCode(),HttpStatus.OK);
    }

    @Test
    public void getSystemsDeploymentByPostDeploymentStatus_inValidStatus() {
        assertEquals(controller.getSystemsDeploymentByPostDeploymentStatus("").getStatusCode(),HttpStatus.BAD_REQUEST);
    }

    @Test
    public void getAllDeploymentsBySystem_validSystem() {
        assertEquals(controller.getAllDeploymentsBySystem("CPS").getStatusCode(),HttpStatus.OK);
    }

    @Test
    public void getAllDeploymentsBySystem_inValidSystem() {
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

        List<SystemDeployment> deploymentList = new ArrayList<>();
        deploymentList.add(new SystemDeployment());

        when(controllerHelper.getDate(anyString())).thenReturn(new Date());
        when(systemDeploymentService.getDeploymentsByDateRange(controllerHelper.getDate(dateFrom), controllerHelper.getDate(dateTo))).thenReturn(deploymentList);
        assertEquals(controller.getAllSystemDeploymentsWithinDateRange(dateFrom, dateTo).getStatusCode(), HttpStatus.OK);
    }


}
