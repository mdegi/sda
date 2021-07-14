"# deploymentsManagement" 

The files need to run the application are as follows:
- 20201106-export: deployments window exported file
- DeploymentsAmanagement.postman_collection: postman requests
- depmgr.postman_environment.json: postman enviroment settings
- deploymentsmanagementconfigservice.tar: spring boot server that provides connection to cloud config and provides this service to other applications
- deploymentsmanagementcontroller.tar: spring boot app to processes file changes and interacts with MongoDB

To run the dockerised application pls first make sure that you have a running mongodb container on port 2016.
- pull mongoDB container
  command: docker pull mongo
- run mongoDB container on port 27016
  <br/>command: docker run --name=deploymentsMongo -d -it -p 27016:27017 -t mongo

deploymentsmanagementconfigservice can be loaded and run by:
- command: docker load -i deploymentsmanagementconfigservice.tar
- command: docker run --name=deploymentsConfigService -d -p 8182:8182 -it depmgr/deploymentsmanagementconfigservice:latest

deploymentsmanagementcontroller can be loaded by: 
- command: docker load -i deploymentsmanagementcontroller.tar
- command: docker run --name=deploymentsController -d -p 8080:8080 -it --mount src="PATH TO CSV FILES",dst=/depExports,type=bind depmgr/deploymentsmanagementcontroller:latest
  <br/>"PATH TO CSV FILES" refer to folder in which file: 20201106-export.csv and any similar files are found 
  <br/>examples of "PATH TO CSV FILES" might be: /c/depExports or "/c/Users/User.Name/OneDrive - GO PLC-/depExports" if the file location is in SharePoint

  
At this point the application should be running and accepting requests on port 8080. 
The postman requests provided, if executed should provide a valid response. 

If the CSV file in mounted folder is deleted, the data representing the file content is automatically deleted by the scheduler  which can be verified running the postman requests just after
Similarly if the CSV file is amended, its contents will be reflected when executing the postman requests
Also, other files can also be added to the folder and will be automatically uploaded and reflected in the postman requests

N.B: CSV file names should be in the format YYYYmmDD-export.csv
