package mt.com.go.deploymentsmanagement.service;

import mt.com.go.deploymentsmanagement.dao.SystemDeploymentRepo;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SystemDeploymentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemDeploymentService.class);

    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    private final String COLLECTION_SYSTEM_DEPLOYMENT = "systemDeploymentRepo";

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void insertRecord(SystemDeploymentRepo systemDeploymentRepo) {
        LOGGER.info("Adding new record: {} - {}", systemDeploymentRepo.getDeploymentDate(), systemDeploymentRepo.getSystemName());
        mongoTemplate.insert(systemDeploymentRepo);
    }

    public void deleteAllRecords() {
        LOGGER.info("Initialising Deployments Table");
        mongoTemplate.dropCollection(COLLECTION_SYSTEM_DEPLOYMENT);
    }

    public void deleteRecords(Date deploymentDate) {
        LOGGER.info("Deleting records for date: {}", deploymentDate);
        Bson filter = new Document("deploymentDate", deploymentDate);
        mongoTemplate.getCollection(COLLECTION_SYSTEM_DEPLOYMENT).deleteMany(filter);
    }

    public List<SystemDeploymentRepo> getDeploymentsByDate(Date deploymentDate) {
        LOGGER.info("Querying records for date: {}", deploymentDate);
        Query deploymentsByDateQuery = query(where("deploymentDate").is(deploymentDate));
        return mongoTemplate.find(deploymentsByDateQuery, SystemDeploymentRepo.class);
    }

    public List<SystemDeploymentRepo> getDeploymentsBySystem(String systemName) {
        LOGGER.info("Querying records for system: {}", systemName);
        Query deploymentsBySystemQuery = query(where("systemName").is(systemName));
        return mongoTemplate.find(deploymentsBySystemQuery, SystemDeploymentRepo.class);
    }

    public List<SystemDeploymentRepo> getDeploymentsWithPostDeploymentStatus(String status) {
        LOGGER.info("Querying records for status: {}", status);
        final String DEV_STATUS_TASKS_REGEX = ".*" + status + "";
        Query systemsWithUnCompletedDevTasksQuery = query(where("devPostDeploymentTasks").regex(DEV_STATUS_TASKS_REGEX));
        return mongoTemplate.find(systemsWithUnCompletedDevTasksQuery, SystemDeploymentRepo.class);
    }

    public List<SystemDeploymentRepo> getDeploymentsByDateRange(Date dateFrom, Date dateTo) {
        LOGGER.info("Querying records for dateRange: {} - {}", dateFrom, dateTo);
        Query systemsByDateRangeQuery = query(where("deploymentDate").gte(dateFrom).lte(dateTo));
        return mongoTemplate.find(systemsByDateRangeQuery, SystemDeploymentRepo.class);
    }

}


