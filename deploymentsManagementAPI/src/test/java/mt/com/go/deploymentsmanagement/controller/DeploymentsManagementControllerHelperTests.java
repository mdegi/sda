package mt.com.go.deploymentsmanagement.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DeploymentsManagementControllerHelperTests {

    DeploymentsManagementControllerHelper controllerHelper;

    @Before
    public void init() {
        controllerHelper = new DeploymentsManagementControllerHelper();
    }

    @Test
    public void getDate_validFormat() {
        assertNotNull(controllerHelper.getDate("20201106"));
    }

    @Test
    public void getDate_inValidFormat() {
        assertNull(controllerHelper.getDate("1324"));
    }
}
