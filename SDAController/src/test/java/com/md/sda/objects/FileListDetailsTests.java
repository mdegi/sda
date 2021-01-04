package com.md.sda.objects;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashSet;

import static org.junit.Assert.*;

public class FileListDetailsTests {

    private FileListDetails fileListDetails;

    @Before
    public void init() {
        fileListDetails = new FileListDetails();
        ReflectionTestUtils.setField(fileListDetails, "newFiles", new HashSet<OSFile>());
        ReflectionTestUtils.setField(fileListDetails, "changedFiles", new HashSet<OSFile>());
        ReflectionTestUtils.setField(fileListDetails, "deletedFiles", new HashSet<OSFile>());
        ReflectionTestUtils.setField(fileListDetails, "noChangeFiles", new HashSet<OSFile>());
    }

    @Test
    public void fileChangesOccurred_shouldReturnFalse() {
        assertFalse(fileListDetails.fileChangesOccurred());
    }

    @Test
    public void fileChangesOccurred_shouldReturnTrue() {
        fileListDetails.getChangedFiles().add(new OSFile("fileName-1", "csv", "/tmp", 132456789, 1345645646));
        fileListDetails.getDeletedFiles().add(new OSFile("fileName-2", "csv", "/tmp", 132456789, 1345645646));
        fileListDetails.getNewFiles().add(new OSFile("fileName-3", "csv", "/tmp", 132456789, 1345645646));
        fileListDetails.getNoChangeFiles().add(new OSFile("fileName-4", "csv", "/tmp", 132456789, 1345645646));
        assertTrue(fileListDetails.fileChangesOccurred());
    }

}
