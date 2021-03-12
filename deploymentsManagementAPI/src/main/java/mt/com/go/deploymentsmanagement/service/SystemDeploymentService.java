package mt.com.go.deploymentsmanagement.service;

import mt.com.go.deploymentsmanagement.dao.SystemDeploymentDAO;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class SystemDeploymentService {

    private MongoTemplate mongoTemplate;

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void insertRecord(SystemDeploymentDAO systemDeploymentDAO) {
        mongoTemplate.insert(systemDeploymentDAO);
    }

    public void deleteRecords(Date deploymentDate) {
        Bson filter = new Document("deploymentDate", deploymentDate);
        String COLLECTION_SYSTEM_DEPLOYMENT = "systemDeployment";
        mongoTemplate.getCollection(COLLECTION_SYSTEM_DEPLOYMENT).deleteMany(filter);
    }

    public List<SystemDeploymentDAO> getDeploymentsByDate(Date deploymentDate) {
        Query deploymentsByDateQuery = query(where("deploymentDate").is(deploymentDate));
        return mongoTemplate.find(deploymentsByDateQuery, SystemDeploymentDAO.class);
    }

    public List<SystemDeploymentDAO> getDeploymentsBySystem(String systemName) {
        Query deploymentsBySystemQuery = query(where("systemName").is(systemName));
        return mongoTemplate.find(deploymentsBySystemQuery, SystemDeploymentDAO.class);
    }

    public List<SystemDeploymentDAO> getDeploymentsWithPostDeploymentStatus(String status) {
        final String DEV_STATUS_TASKS_REGEX = ".*" + status + "";
        Query systemsWithUnCompletedDevTasksQuery = query(where("devPostDeploymentTasks").regex(DEV_STATUS_TASKS_REGEX));
        return mongoTemplate.find(systemsWithUnCompletedDevTasksQuery, SystemDeploymentDAO.class);
    }

    public List<SystemDeploymentDAO> getDeploymentsByDateRange(Date dateFrom, Date dateTo) {
        Query systemsByDateRangeQuery = query(where("deploymentDate").gte(dateFrom).lte(dateTo));
        return mongoTemplate.find(systemsByDateRangeQuery, SystemDeploymentDAO.class);
    }

}


