package com.md.sda.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
public class SystemDeployment {

    @Id
    public String id;

    public String systemName;;
    public String developer;
    public String peerReviewer;
    public String status;

    public SystemDeployment() {}

    public SystemDeployment(String systemName, String developer, String peerReviewer, String status) {
        this.systemName = systemName;
        this.developer = developer;
        this.peerReviewer = peerReviewer;
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format(
                "SystemDeployment[id=%s, systemName='%s', developer='%s', peerReviewer='%s', peerReviewer='%s']",
                id, systemName, developer, peerReviewer, status);
    }

}