package mt.com.go.deploymentsmanagement.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Getter;
import lombok.Setter;
import mt.com.go.deploymentsmanagement.objects.OSFile;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Component
public class CSVDeploymentEntry implements DeploymentEntry {

    @CsvBindByName(column = "#")
    private int lineNumber;

    @CsvBindByName (column = "Sponsor")
    private String sponsor;

    @CsvBindByName (column = "Status")
    private String status;

    @CsvBindByName (column = "Deployed to staging server?")
    private String stagingStatus;

    @CsvBindByName (column = "System/Component to be deployed")
    private String systemName;

    @CsvBindByName (column = "Project/Initiative")
    private String projectInitiative;

    @CsvBindByName (column = "Deployment Instructions(and post-deployment tests)")
    private String deploymentInstructions;

    @CsvBindByName (column = "Dependencies on other deployments")
    private String dependencies;

    @CsvBindByName (column = "Release Notes")
    private String releaseNotes;

    @CsvBindByName (column = "Contact Person")
    private String contactPerson;

    @CsvBindByName (column = "Peer Reviewer")
    private String peerReviewer;

    @CsvBindByName (column = "Actual Deployment Duration on Staging(Minutes)")
    private String actualSTGDeploymentDurationMinutes;

    @CsvBindByName (column = "Projected Deployment Duration on Production(Minutes)")
    private int projectedDurationMinutes;

    @CsvBindByName (column = "Actual Deployment Duration on Production(Minutes)")
    private int actualProdDeploymentDurationMinutes;

    @CsvBindByName (column = "Can be done during the day? (Yes/No)")
    private String canBeDoneDuringTheDay;

    @CsvBindByName (column = "Deployment Application Date")
    private String deploymentApplicationDate;

    @CsvBindByName (column = "Deployment Automation (Full/Partial/None)")
    private String deploymentAutomation;

    @CsvBindByName (column = "Dev post-deployment Jira tasks (see here)")
    private String devPostDeploymentTasks;

    public List<DeploymentEntry> getDeploymentEntries(OSFile osFile) {
        try {
            List<CSVDeploymentEntry> entries;
            Reader reader = new BufferedReader(new FileReader(osFile.getFullPath()));
            CsvToBean<CSVDeploymentEntry> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(CSVDeploymentEntry.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            entries = csvToBean.parse();

            return entries.stream()
                    .map(csvEntry -> (DeploymentEntry)csvEntry)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }


}
