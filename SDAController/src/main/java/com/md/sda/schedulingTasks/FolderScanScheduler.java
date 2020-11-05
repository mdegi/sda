package com.md.sda.schedulingTasks;
import com.md.sda.config.AppConfig;
import com.md.sda.objects.FileListDetails;
import com.md.sda.objects.OSFile;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableScheduling
@ManagedResource(objectName = "jmxSDAController:name=ReadFolderSchedulerMBean")
public class FolderScanScheduler {

    private static final Logger log = LoggerFactory.getLogger(FolderScanScheduler.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private Set<OSFile> lastScannedFileSet;
    private final AppConfig appConfig;

    public FolderScanScheduler(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    //a scheduled method should have the void return type
    //a method should not accept any parameters
    //options are fixedRate / fixedDelay and cron
    @Scheduled(fixedDelayString = "${fileScanFixedRateMilliSeconds}", initialDelayString = "${fileScanInitialDelayMilliSeconds}")
    public void fileChangesScheduler() {
        log.info("The time is now {}", dateFormat.format(new Date()));
        boolean filesScanned = processFileChangesIfAny();
    }

    @ManagedOperation
    public boolean processFileChangesIfAny() {
        FileListDetails comparedFiles = new FileListDetails();

        if (lastScannedFileSet == null) {
            lastScannedFileSet = new HashSet<>();
        }

        try {
            Set<Path> currentFileList = getFilePaths(appConfig.getFileSystemPath(), 1);
            if (lastScannedFileSet.isEmpty()) {
                lastScannedFileSet.addAll(currentFileList.stream().map(this::getOSFile).collect(Collectors.toSet()));
                comparedFiles.getNewFiles().addAll(lastScannedFileSet);
                return true;
            }
            comparedFiles = compareFiles(currentFileList);
            lastScannedFileSet.clear();
            lastScannedFileSet.addAll(comparedFiles.getNewFiles());
            lastScannedFileSet.removeAll(comparedFiles.getDeletedFiles());
            lastScannedFileSet.addAll(comparedFiles.getChangedFiles());
            lastScannedFileSet.addAll(comparedFiles.getNoChangeFiles());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private Set<Path> getFilePaths(String dir, int depth) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .collect(Collectors.toSet());
        }
    }

    private OSFile getOSFile(Path path) {
        long createdTime = 0;
        long modifiedTime = 0;

        try {
            BasicFileAttributes fileAttributes = Files.readAttributes(path, BasicFileAttributes.class);
            createdTime = fileAttributes.creationTime().toMillis();
            modifiedTime = fileAttributes.lastModifiedTime().toMillis();
        } catch (IOException e) {
            log.error("Error getting File Attributes-:\n" + e.getMessage());
        }

        return new OSFile(
                path.getFileName().toString(),
                FilenameUtils.getExtension((path.getFileName().toString())),
                path.toAbsolutePath().toString(),
                modifiedTime,
                createdTime
        );
    }

    private Set<OSFile> getOSFiles(Set<OSFile> fileMasterSet, Set<OSFile> fileSubSet) {
        return fileMasterSet.stream()
                .filter(path -> fileSubSet.stream().noneMatch(lastFile -> lastFile.getFileName().equals(path.getFileName())))
                .collect(Collectors.toSet());
    }

    private FileListDetails compareFiles(Set<Path> currentFileList) {
        FileListDetails compareResult = new FileListDetails();

        compareResult.getNewFiles().addAll(getOSFiles(currentFileList.stream().map(this::getOSFile).collect(Collectors.toSet()), lastScannedFileSet));
        compareResult.getDeletedFiles().addAll(getOSFiles(lastScannedFileSet, currentFileList.stream().map(this::getOSFile).collect(Collectors.toSet())));

        Set<OSFile> existingFiles = currentFileList.stream()
                .filter(path -> lastScannedFileSet.stream().anyMatch(lastFile -> lastFile.getFileName().equals(path.getFileName().toString())))
                .map(this::getOSFile)
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

}
