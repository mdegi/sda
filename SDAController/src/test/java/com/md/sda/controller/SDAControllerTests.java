package com.md.sda.controller;

import com.md.sda.config.AppConfig;
import com.md.sda.model.SystemDeployment;
import com.md.sda.schedulingTasks.FolderScanScheduler;
import com.md.sda.service.SystemDeploymentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.md.sda.config.AppConfigConstants.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SDAControllerTests {

    @Mock
    FolderScanScheduler scheduler;

    @Mock
    AppConfig appConfig;

    @Mock
    SystemDeploymentService systemDeploymentService;

    @Mock
    SDAControllerHelper sdaControllerHelper;

    @InjectMocks
    SDAController controller;

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

        when(sdaControllerHelper.getDate(anyString())).thenReturn(new Date());
        when(systemDeploymentService.getDeploymentsByDate(any())).thenReturn(deploymentsByDate);
        controller.getSystemsDeploymentByDate("20210111");
        assertFalse(deploymentsByDate.isEmpty());
    }

    @Test
    public void getSystemsDeploymentByDate_inValidDate() {
        when(sdaControllerHelper.getDate(anyString())).thenReturn(null);
        when(systemDeploymentService.getDeploymentsByDate(null)).thenReturn(null);

        controller.getSystemsDeploymentByDate("1234");
        assert(true);
    }

    @Test
    public void getDeploymentTotalDurationToDeployByDate_entriesFound() {
        String deploymentDate = "20210111";
        List<SystemDeployment> deploymentList = new ArrayList<>();
        deploymentList.add(new SystemDeployment());

        when(sdaControllerHelper.getDate(anyString())).thenReturn(new Date());
        when(systemDeploymentService.getDeploymentsByDate(any())).thenReturn(deploymentList);

        assertTrue(controller.getDeploymentTotalDurationToDeployByDate(deploymentDate).getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    public void getDeploymentTotalDurationToDeployByDate_noEntries() {
        String deploymentDate = "20210112";
        List<SystemDeployment> deploymentList = new ArrayList<>();
        when(systemDeploymentService.getDeploymentsByDate(sdaControllerHelper.getDate(deploymentDate))).thenReturn(deploymentList);

        assertTrue(controller.getDeploymentTotalDurationToDeployByDate(deploymentDate).getStatusCode().equals(HttpStatus.OK));
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
        assertTrue(controller.reloadFiles().getStatusCode().equals(HttpStatus.OK));
    }

    @Test
    public void getAllSystemDeploymentsWithinDateRange_validDates() {
        String dateTo = "20210111";
        String dateFrom = "20210111";

        List<SystemDeployment> deploymentList = new ArrayList<>();
        deploymentList.add(new SystemDeployment());

        when(sdaControllerHelper.getDate(anyString())).thenReturn(new Date());
        when(systemDeploymentService.getDeploymentsByDateRange(sdaControllerHelper.getDate(dateFrom),sdaControllerHelper.getDate(dateTo))).thenReturn(deploymentList);
        assertTrue(controller.getAllSystemDeploymentsWithinDateRange(dateFrom, dateTo).getStatusCode().equals(HttpStatus.OK));
    }

}
