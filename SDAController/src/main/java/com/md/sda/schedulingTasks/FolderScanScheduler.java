package com.md.sda.schedulingTasks;

import com.md.sda.config.AppConfig;
import com.md.sda.model.CSVDeploymentEntry;
import com.md.sda.model.SystemDeployment;
import com.md.sda.objects.FileListDetails;
import com.md.sda.objects.OSFile;
import com.md.sda.service.SystemDeploymentService;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
@EnableScheduling
@ManagedResource(objectName = "jmxSDAController:name=ReadFolderSchedulerMBean")
public class FolderScanScheduler {

    final MongoTemplate mongoTemplate;

    private static final Logger log = LoggerFactory.getLogger(FolderScanScheduler.class);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private Set<OSFile> lastScannedFileSet;
    private final AppConfig appConfig;
    private final SystemDeploymentService systemDeploymentService;

    public FolderScanScheduler(AppConfig appConfig, SystemDeploymentService systemDeploymentService, MongoTemplate mongoTemplate) {
        this.appConfig = appConfig;
        this.systemDeploymentService = systemDeploymentService;
        if (systemDeploymentService.getMongoTemplate() == null) {
            systemDeploymentService.setMongoTemplate(mongoTemplate);
        }
        this.mongoTemplate = mongoTemplate;
    }

    //a scheduled method should have the void return type
    //a method should not accept any parameters
    //options are fixedRate / fixedDelay and cron
    @Scheduled(fixedDelayString = "${fileScanFixedRateMilliSeconds}", initialDelayString = "${fileScanInitialDelayMilliSeconds}")
    public void fileChangesScheduler() {
        log.info("The time is now {}", timeFormat.format(new Date()));
        processFileChangesIfAny();
    }

    @ManagedOperation
    public void processFileChangesIfAnyJMX() {
        log.info("External call through JMX to process any file changes {}", timeFormat.format(new Date()));
        processFileChangesIfAny();
    }

    public void processFileChangesIfAnyREST() {
        log.info("REST call to process any file changes {}", timeFormat.format(new Date()));
        processFileChangesIfAny();
    }

    public void processFileChangesIfAny() {
        FileListDetails comparedFiles = new FileListDetails();

        if (lastScannedFileSet == null) {
            lastScannedFileSet = new HashSet<>();
        }

        try {
            int directoryScanLevel_1 = 1;
            Set<Path> currentFileList = getFilePaths(appConfig.getFileSystemPath(), directoryScanLevel_1, appConfig.getFilenameRegex(), appConfig.getFileExtension());
            if (lastScannedFileSet.isEmpty()) {
                lastScannedFileSet.addAll(currentFileList.stream().map(this::getOSFile).collect(Collectors.toSet()));
                comparedFiles.getNewFiles().addAll(lastScannedFileSet);
            } else {
                comparedFiles = compareFiles(currentFileList);
                lastScannedFileSet.clear();
                lastScannedFileSet.addAll(comparedFiles.getNewFiles());
                lastScannedFileSet.removeAll(comparedFiles.getDeletedFiles());
                lastScannedFileSet.addAll(comparedFiles.getChangedFiles());
                lastScannedFileSet.addAll(comparedFiles.getNoChangeFiles());
            }
            if (comparedFiles.fileChangesOccured()) {
                if (!comparedFiles.getNewFiles().isEmpty()) {
                    //delete any saved documents with the new file name in cases these exist
                    comparedFiles.getNewFiles().forEach(newFile -> deleteEntries(getDeploymentDateFromFileName(newFile.getFileName())));
                    comparedFiles.getNewFiles().forEach(newFile -> getDeploymentEntries(newFile).forEach(csvDeploymentEntry -> saveEntry(csvDeploymentEntry, getDeploymentDateFromFileName(newFile.getFileName()))));
                }
                if (!comparedFiles.getDeletedFiles().isEmpty()) {
                    comparedFiles.getDeletedFiles().forEach(deletedFile -> deleteEntries(getDeploymentDateFromFileName(deletedFile.getFileName())));
                }
                if (!comparedFiles.getChangedFiles().isEmpty()) {
                    comparedFiles.getChangedFiles().forEach(changedFile -> deleteEntries(getDeploymentDateFromFileName(changedFile.getFileName())));
                    comparedFiles.getChangedFiles().forEach(changedFile -> getDeploymentEntries(changedFile).forEach(csvDeploymentEntry -> saveEntry(csvDeploymentEntry, getDeploymentDateFromFileName(changedFile.getFileName()))));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Date getDeploymentDateFromFileName(String fileName) {
        return getDate(fileName.substring(0,fileName.indexOf("-")));
    }

    private void deleteEntries(Date deploymentDate) {
        log.info("Deleting DB Entries: " + deploymentDate);

        systemDeploymentService.deleteRecords(deploymentDate);
    }

    private void saveEntry(CSVDeploymentEntry csvDeploymentEntry, Date deploymentDate) {
        log.info("Saving CSV Entry: " + deploymentDate + " - " + csvDeploymentEntry.getSystemName());

        SystemDeployment systemDeployment = new SystemDeployment();
        systemDeployment.setLineNumber(csvDeploymentEntry.getLineNumber());
        systemDeployment.setSponsor(csvDeploymentEntry.getSponsor());
        systemDeployment.setStatus(csvDeploymentEntry.getStatus());
        systemDeployment.setStagingStatus(csvDeploymentEntry.getStagingStatus());
        systemDeployment.setSystemName(csvDeploymentEntry.getSystemName());
        systemDeployment.setProjectInitiative(csvDeploymentEntry.getProjectInitiative());
        systemDeployment.setDeploymentInstructions(csvDeploymentEntry.getDeploymentInstructions());
        systemDeployment.setDependencies(csvDeploymentEntry.getDependencies());
        systemDeployment.setReleaseNotes(csvDeploymentEntry.getReleaseNotes());
        systemDeployment.setContactPerson(csvDeploymentEntry.getContactPerson());
        systemDeployment.setPeerReviewer(csvDeploymentEntry.getPeerReviewer());
        systemDeployment.setActualSTGDeploymentDurationMinutes(csvDeploymentEntry.getActualSTGDeploymentDurationMinutes());
        systemDeployment.setProjectedDurationMinutes(csvDeploymentEntry.getProjectedDurationMinutes());
        systemDeployment.setActualProdDeploymentDurationMinutes(csvDeploymentEntry.getActualProdDeploymentDurationMinutes());
        systemDeployment.setCanBeDoneDuringTheDay(csvDeploymentEntry.getCanBeDoneDuringTheDay());
        systemDeployment.setDeploymentApplicationDate(csvDeploymentEntry.getDeploymentApplicationDate());
        systemDeployment.setDeploymentAutomation(csvDeploymentEntry.getDeploymentAutomation());
        systemDeployment.setDevPostDeploymentTasks(csvDeploymentEntry.getDevPostDeploymentTasks());
        systemDeployment.setDeploymentDate(deploymentDate);

        systemDeploymentService.insertRecord(systemDeployment);
    }

    private Date getDate(String deploymentDate) {
        Date parsedDate = null;

        try {
            parsedDate = dateFormat.parse(deploymentDate);
        } catch (ParseException e) {
            log.error("ParseException parsing date: " + deploymentDate);
        }

        return parsedDate;
    }

    private List<CSVDeploymentEntry> getDeploymentEntries(OSFile osFile) {

        List<CSVDeploymentEntry> entries = null;
        try (
                Reader reader = new BufferedReader(new FileReader(osFile.getFullPath()));
        ) {
            CsvToBean<CSVDeploymentEntry> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CSVDeploymentEntry.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            entries = csvToBean.parse();
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException envountered: " + e.getMessage());
        } catch (IOException e) {
            log.error("IOException envountered: " + e.getMessage());
        }

        return entries;
    }

    private Set<Path> getFilePaths(String dir, int depth, String fileNameRegex, String fileType) throws IOException {
        try (Stream<Path> stream = Files.walk(Paths.get(dir), depth)) {
            return stream
                    .filter(file -> !Files.isDirectory(file))
                    .filter(file -> file.getFileName().toString().substring(0, file.getFileName().toString().length() - fileType.length() -1).matches(fileNameRegex))
                    .filter(file -> fileType.equals(file.getFileName().toString().substring(file.getFileName().toString().length() - fileType.length())))
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
