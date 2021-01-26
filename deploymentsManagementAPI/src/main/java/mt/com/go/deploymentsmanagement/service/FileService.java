package mt.com.go.deploymentsmanagement.service;

import mt.com.go.deploymentsmanagement.config.AppConfig;
import mt.com.go.deploymentsmanagement.objects.FileListDetails;
import mt.com.go.deploymentsmanagement.objects.OSFile;

import java.util.Set;

public interface FileService {

    public Set<OSFile> getFiles();

    public FileListDetails compareFiles(Set<OSFile> currentFileList, Set<OSFile> lastScannedFileSet);

    public void setAppConfig(AppConfig appConfig);

}
