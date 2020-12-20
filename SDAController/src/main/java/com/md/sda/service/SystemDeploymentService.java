package com.md.sda.service;

import com.md.sda.model.SystemDeployment;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component("systemDeploymentService")
public class SystemDeploymentService {

    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void insertRecord(SystemDeployment systemDeployment) {
        mongoTemplate.insert(systemDeployment);
    }

    /// fix this
    public void deleteRecords(String deploymentDate) {
        Bson filter = new Document("deploymentDate", deploymentDate);
        mongoTemplate.getCollection("systemDeployment").deleteMany(filter);
    }

}

