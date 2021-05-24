package mt.com.go.deploymentsmanagement.schedulingTasks;

import mt.com.go.deploymentsmanagement.config.AppConfig;
import mt.com.go.deploymentsmanagement.model.DeploymentEntry;
import mt.com.go.deploymentsmanagement.dao.SystemDeploymentRepo;
import mt.com.go.deploymentsmanagement.objects.FileListDetails;
import mt.com.go.deploymentsmanagement.objects.OSFile;
import mt.com.go.deploymentsmanagement.service.FileService;
import mt.com.go.deploymentsmanagement.service.SystemDeploymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableScheduling
@ManagedResource(objectName = "jmxDeploymentsManagementController:name=ReadFolderSchedulerMBean")
public class FolderScanScheduler {

    final MongoTemplate mongoTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(FolderScanScheduler.class);
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    private Set<OSFile> lastScannedFileSet;
    private final SystemDeploymentService systemDeploymentService;
    private final FileService fileService;

    private final DeploymentEntry deploymentEntry;

    public FolderScanScheduler(AppConfig appConfig, SystemDeploymentService systemDeploymentService,
                               MongoTemplate mongoTemplate, DeploymentEntry deploymentEntry, FileService fileService) {
        this.systemDeploymentService = systemDeploymentService;
        if (systemDeploymentService.getMongoTemplate() == null) {
            systemDeploymentService.setMongoTemplate(mongoTemplate);
        }
        this.mongoTemplate = mongoTemplate;
        this.deploymentEntry = deploymentEntry;
        this.fileService = fileService;
        fileService.setAppConfig(appConfig);
        systemDeploymentService.deleteAllRecords();
    }

    //a scheduled method should have the void return type
    //such method should not accept any parameters
    //options are fixedRate / fixedDelay and cron
    @Scheduled(fixedDelayString = "${fileScanFixedRateMilliSeconds}", initialDelayString = "${fileScanInitialDelayMilliSeconds}")
    public void fileChangesScheduler() {
        LOGGER.info("The time is now {}", timeFormat.format(new Date()));
        processFileChangesIfAny();
    }

    @ManagedOperation
    public void processFileChangesIfAnyJMX() {
        LOGGER.info("External call through JMX to process any file changes {}", timeFormat.format(new Date()));
        processFileChangesIfAny();
    }

    public void processFileChangesIfAnyREST() {
        LOGGER.info("REST call to process any file changes {}", timeFormat.format(new Date()));
        processFileChangesIfAny();
    }

    public void processFileChangesIfAny() {
        LOGGER.info("Processing any file changes");
        FileListDetails comparedFiles = new FileListDetails();

        if (lastScannedFileSet == null) {
            lastScannedFileSet = new HashSet<>();
        }

        Set<OSFile> currentOsFileLists = fileService.getFiles();
        if (lastScannedFileSet.isEmpty()) {
            lastScannedFileSet.addAll(currentOsFileLists);
            comparedFiles.getNewFiles().addAll(lastScannedFileSet);
        } else {
            comparedFiles = fileService.compareFiles(currentOsFileLists, lastScannedFileSet);
            lastScannedFileSet.clear();
            lastScannedFileSet.addAll(comparedFiles.getNewFiles());
            lastScannedFileSet.removeAll(comparedFiles.getDeletedFiles());
            lastScannedFileSet.addAll(comparedFiles.getChangedFiles());
            lastScannedFileSet.addAll(comparedFiles.getNoChangeFiles());
        }
        if (comparedFiles.fileChangesOccurred()) {
            if (!comparedFiles.getNewFiles().isEmpty()) {
                //delete any saved documents with the new file name in cases these exist
                comparedFiles.getNewFiles().forEach(newFile -> deleteEntries(getDeploymentDateFromFileName(newFile.getFileName())));
                comparedFiles.getNewFiles().forEach(newFile -> deploymentEntry.getDeploymentEntries(newFile).forEach(dpEntry -> saveEntry(dpEntry, getDeploymentDateFromFileName(newFile.getFileName()))));
            }
            if (!comparedFiles.getDeletedFiles().isEmpty()) {
                comparedFiles.getDeletedFiles().forEach(deletedFile -> deleteEntries(getDeploymentDateFromFileName(deletedFile.getFileName())));
            }
            if (!comparedFiles.getChangedFiles().isEmpty()) {
                comparedFiles.getChangedFiles().forEach(changedFile -> deleteEntries(getDeploymentDateFromFileName(changedFile.getFileName())));
                comparedFiles.getChangedFiles().forEach(changedFile -> deploymentEntry.getDeploymentEntries(changedFile).forEach(dpEntry -> saveEntry(dpEntry, getDeploymentDateFromFileName(changedFile.getFileName()))));
            }
        }
    }

    private Date getDeploymentDateFromFileName(String fileName) {
        return getDate(fileName.substring(0,fileName.indexOf("-")));
    }

    private void deleteEntries(Date deploymentDate) {
        LOGGER.info("Deleting DB Entries: " + deploymentDate);
        systemDeploymentService.deleteRecords(deploymentDate);
    }

    private void saveEntry(DeploymentEntry deploymentEntry, Date deploymentDate) {
        LOGGER.info("Saving Deployment Entry: " + deploymentDate + " - " + deploymentEntry.getSystemName());

        SystemDeploymentRepo systemDeploymentRepo = new SystemDeploymentRepo();
        systemDeploymentRepo.setLineNumber(deploymentEntry.getLineNumber());
        systemDeploymentRepo.setSponsor(deploymentEntry.getSponsor());
        systemDeploymentRepo.setStatus(deploymentEntry.getStatus());
        systemDeploymentRepo.setStagingStatus(deploymentEntry.getStagingStatus());
        systemDeploymentRepo.setSystemName(deploymentEntry.getSystemName());
        systemDeploymentRepo.setProjectInitiative(deploymentEntry.getProjectInitiative());
        systemDeploymentRepo.setDeploymentInstructions(deploymentEntry.getDeploymentInstructions());
        systemDeploymentRepo.setDependencies(deploymentEntry.getDependencies());
        systemDeploymentRepo.setReleaseNotes(deploymentEntry.getReleaseNotes());
        systemDeploymentRepo.setContactPerson(deploymentEntry.getContactPerson());
        systemDeploymentRepo.setPeerReviewer(deploymentEntry.getPeerReviewer());
        systemDeploymentRepo.setActualSTGDeploymentDurationMinutes(deploymentEntry.getActualSTGDeploymentDurationMinutes());
        systemDeploymentRepo.setProjectedDurationMinutes(deploymentEntry.getProjectedDurationMinutes());
        systemDeploymentRepo.setActualProdDeploymentDurationMinutes(deploymentEntry.getActualProdDeploymentDurationMinutes());
        systemDeploymentRepo.setCanBeDoneDuringTheDay(deploymentEntry.getCanBeDoneDuringTheDay());
        systemDeploymentRepo.setDeploymentApplicationDate(deploymentEntry.getDeploymentApplicationDate());
        systemDeploymentRepo.setDeploymentAutomation(deploymentEntry.getDeploymentAutomation());
        systemDeploymentRepo.setDevPostDeploymentTasks(deploymentEntry.getDevPostDeploymentTasks());
        systemDeploymentRepo.setDeploymentDate(deploymentDate);

        systemDeploymentService.insertRecord(systemDeploymentRepo);
    }

    private Date getDate(String deploymentDate) {
        Date parsedDate = null;

        try {
            parsedDate = dateFormat.parse(deploymentDate);
        } catch (ParseException e) {
            LOGGER.error("ParseException parsing date: " + deploymentDate);
        }
        return parsedDate;
    }
}
