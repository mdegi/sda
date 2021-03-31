package mt.com.go.deploymentsmanagement.objects;

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

    public boolean fileChangesOccurred() {
        return (!newFiles.isEmpty() || !changedFiles.isEmpty() || !deletedFiles.isEmpty());
    }

    public String getFileNames(Set<OSFile> fileSet) {
        return String.join(",", fileSet.stream().map(OSFile::getFileName).toArray(String[]::new));
    }

}
