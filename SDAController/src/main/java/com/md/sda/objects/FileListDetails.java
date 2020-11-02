package com.md.sda.objects;

import lombok.Getter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
public class FileListDetails implements Serializable {

    private Set<OSFile> newFiles;
    private Set<OSFile> changedFiles;
    private Set<OSFile> deletedFiles;
    private Set<OSFile> noChangeFiles;

    public FileListDetails() {
        newFiles = new HashSet<>();
        changedFiles = new HashSet<>();
        deletedFiles = new HashSet<>();
        noChangeFiles = new HashSet<>();
    }

}
