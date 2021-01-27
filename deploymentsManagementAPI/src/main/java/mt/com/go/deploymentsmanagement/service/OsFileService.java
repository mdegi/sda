package mt.com.go.deploymentsmanagement.service;

import lombok.SneakyThrows;
import mt.com.go.deploymentsmanagement.config.AppConfig;
import mt.com.go.deploymentsmanagement.objects.FileListDetails;
import mt.com.go.deploymentsmanagement.objects.OSFile;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OsFileService implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OsFileService.class);
    private AppConfig appConfig;
    public void setAppConfig(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @SneakyThrows
    @Override
    public Set<OSFile> getFiles() {
        try (Stream<Path> stream = Files.walk(Paths.get(appConfig.getFileSystemPath()), 1)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.getFileName().toString().substring(0, file.getFileName().toString().length() - appConfig.getFileExtension().length() -1).matches(appConfig.getFilenameRegex()))
                    .filter(file -> appConfig.getFileExtension().equals(file.getFileName().toString().substring(file.getFileName().toString().length() - appConfig.getFileExtension().length())))
                    .map(this::getOSFile)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public FileListDetails compareFiles(Set<OSFile> currentFileList, Set<OSFile> lastScannedFileSet) {
        FileListDetails compareResult = new FileListDetails();

        compareResult.getNewFiles().addAll(getOSFiles(currentFileList, lastScannedFileSet));
        compareResult.getDeletedFiles().addAll(getOSFiles(lastScannedFileSet, currentFileList));

        Set<OSFile> existingFiles = currentFileList.stream()
                .filter(path -> lastScannedFileSet.stream().anyMatch(lastFile -> lastFile.getFileName().equals(path.getFileName())))
                .collect(Collectors.toSet());

        for (OSFile existingFile: existingFiles) {
            for (OSFile lastScannedFile: lastScannedFileSet) {
                if (existingFile.getFileName().equals(lastScannedFile.getFileName())) {
                    if (!lastScannedFile.equals(existingFile)) {
                        compareResult.getChangedFiles().add(existingFile);
                    } else {
                        compareResult.getNoChangeFiles().add(existingFile);
                    }
                }
            }
        }
        return compareResult;
    }

    private Set<OSFile> getOSFiles(Set<OSFile> fileMasterSet, Set<OSFile> fileSubSet) {
        return fileMasterSet.stream()
                .filter(path -> fileSubSet.stream().noneMatch(lastFile -> lastFile.getFileName().equals(path.getFileName())))
                .collect(Collectors.toSet());
    }

    private OSFile getOSFile(Path path) {
        long createdTime = 0;
        long modifiedTime = 0;

        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
            createdTime = fileAttributes.creationTime().toMillis();
            modifiedTime = fileAttributes.lastModifiedTime().toMillis();
        } catch (IOException e) {
            LOGGER.error("Error getting File Attributes-:\n" + e.getMessage());
        }

        return new OSFile(
                path.getFileName().toString(),
                FilenameUtils.getExtension((path.getFileName().toString())),
                path.toAbsolutePath().toString(),
                modifiedTime,
                createdTime
        );
    }

}
