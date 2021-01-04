package com.md.sda.objects;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OSFileTests {

    private OSFile osFile;

    @Before
    public void init() {
        osFile = new OSFile("fileName-1", "csv", "/tmp", 132456789, 1345645646);
    }

    @Test
    public void equals_shouldReturnTrue() {
        OSFile osFile = new OSFile("fileName-1", "csv", "/tmp", 132456789, 1345645646);
        assertTrue(this.osFile.equals(osFile));
    }

    @Test
    public void equals_shouldReturnFalse() {
        OSFile osFile = new OSFile("fileName-2", "csv", "/tmp", 132456789, 1345645646);
        assertFalse(this.osFile.equals(osFile));
    }

    @Test
    public void equals_invalidObject() {
        assertFalse(this.osFile.equals("osFile"));
    }

    @Test
    public void hashCode_shouldReturnInt() {
        Integer integerValue = osFile.hashCode();
        assertEquals((int) integerValue, osFile.hashCode());
    }
}
;
