package com.md.sda.controller;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SDAControllerHelperTests {

    SDAControllerHelper controllerHelper;

    @Before
    public void init() {
        controllerHelper = new SDAControllerHelper();
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
