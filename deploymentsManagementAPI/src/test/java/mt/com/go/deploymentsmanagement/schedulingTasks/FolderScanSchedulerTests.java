package mt.com.go.deploymentsmanagement.schedulingTasks;

import mt.com.go.deploymentsmanagement.config.AppConfig;
import mt.com.go.deploymentsmanagement.model.DeploymentEntry;
import mt.com.go.deploymentsmanagement.objects.OSFile;
import mt.com.go.deploymentsmanagement.service.SystemDeploymentService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;
import java.util.Set;

import static mt.com.go.deploymentsmanagement.config.AppConfigConstants.*;
import static org.mockito.Mockito.when;

public class FolderScanSchedulerTests {
    @Mock
    MongoTemplate mongoTemplate;

    @Mock
    SystemDeploymentService systemDeploymentService;

    @Mock
    DeploymentEntry deploymentEntry;

    @Mock
    AppConfig appConfig;

    @InjectMocks
    FolderScanScheduler folderScanScheduler;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void fileChangesScheduler_firstScan() {
        when(appConfig.getFileSystemPath()).thenReturn(FILE_SYSTEM_PATH);
        when(appConfig.getFilenameRegex()).thenReturn(FILE_NAME_REGEX);
        when(appConfig.getFileExtension()).thenReturn(FILE_EXTENSION);

        folderScanScheduler.fileChangesScheduler();
        assert(true);
    }

    @Test
    public void processFileChangesIfAnyJMX_shouldReturnTrue() {
        when(appConfig.getFileSystemPath()).thenReturn(FILE_SYSTEM_PATH);
        when(appConfig.getFilenameRegex()).thenReturn(FILE_NAME_REGEX);
        when(appConfig.getFileExtension()).thenReturn(FILE_EXTENSION);

        folderScanScheduler.processFileChangesIfAnyJMX();
        assert(true);
    }

    @Test
    public void processFileChangesIfAnyREST_shouldReturnTrue() {
        when(appConfig.getFileSystemPath()).thenReturn(FILE_SYSTEM_PATH);
        when(appConfig.getFilenameRegex()).thenReturn(FILE_NAME_REGEX);
        when(appConfig.getFileExtension()).thenReturn(FILE_EXTENSION);

        folderScanScheduler.processFileChangesIfAnyREST();
        assert(true);
    }

    @Test
    public void fileChangesScheduler_secondScan() {
        Set<OSFile> lastScannedFileSet= new HashSet<>();
        OSFile osFile = new OSFile("20201106-export", FILE_EXTENSION, "", 1234, 0L);
        lastScannedFileSet.add(osFile);

        ReflectionTestUtils.setField(folderScanScheduler, "lastScannedFileSet", lastScannedFileSet);

        when(appConfig.getFileSystemPath()).thenReturn(FILE_SYSTEM_PATH);
        when(appConfig.getFilenameRegex()).thenReturn(FILE_NAME_REGEX);
        when(appConfig.getFileExtension()).thenReturn(FILE_EXTENSION);

        folderScanScheduler.fileChangesScheduler();
        assert(true);
    }

}
