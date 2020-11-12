package com.md.sda.schedulingTasks;


import com.md.sda.objects.FileListDetails;
import com.md.sda.objects.OSFile;

import java.util.Set;

public class FolderScanSchedulerHelper {

    private FileListDetails fileListDetails;

    public FolderScanSchedulerHelper(FileListDetails fileListDetails) {
        this.fileListDetails = fileListDetails;
    }

    public void processFileChanges(FileListDetails fileListDetails){
    }

    public void processNewFiles(Set<OSFile> newFiles){
    }

    public void processDeletedFiles(Set<OSFile> deletedFiles){
    }

    public void processChangedFiles(Set<OSFile> changedFiles){
    }

}
