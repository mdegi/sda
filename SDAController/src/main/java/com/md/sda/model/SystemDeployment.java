package com.md.sda.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document
@Getter
@NoArgsConstructor
public class SystemDeployment {

    @Id
    public String id;

    public Date deploymentDate;;
    private String deploymentDescription;

    private List<AppSystem> appSystems;

    public SystemDeployment(Date deploymentDate, String deploymentDescription, List<AppSystem> appSystems) {
        this.deploymentDate = deploymentDate;
        this.deploymentDescription = deploymentDescription;
        this.appSystems = appSystems;
    }

    @Override
    public String toString() {
        return String.format(
                "SystemDeployment[id=%s, deploymentDate='%s', deploymentDescription='%s', appSystems='%s']",
                id, deploymentDate.toString(), deploymentDescription, appSystems);
    }

}