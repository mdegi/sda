package mt.com.go.deploymentsmanagement.model;

import mt.com.go.deploymentsmanagement.objects.OSFile;
import org.junit.Test;

import static mt.com.go.deploymentsmanagement.config.AppConfigConstants.FILE_EXTENSION;
import static org.junit.Assert.assertThrows;

public class CSVDeploymentEntryTests {

    @Test
    public void getDeploymentEntries_shouldThrowException() {
        OSFile osFile = new OSFile("20201106-export", FILE_EXTENSION, "", 1234, 0L);
        CSVDeploymentEntry csvDeploymentEntry = new CSVDeploymentEntry();
        assertThrows(RuntimeException.class, () -> {csvDeploymentEntry.getDeploymentEntries(osFile);});
    }
}
