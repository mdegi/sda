package com.md.sda.service;

import com.md.sda.model.SystemDeployment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component("systemDeploymentService")
public class SystemDeploymentService {

    private MongoTemplate mongoTemplate;

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void insertRecord(SystemDeployment systemDeployment) {
        mongoTemplate.insert(systemDeployment);
    }

}

