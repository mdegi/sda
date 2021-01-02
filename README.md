"# sda" 

Files related to the application can be found at:
https://goplc-my.sharepoint.com/:f:/g/personal/martin_degiorgio_go_com_mt/Eglz2aprNZtMr2A-uDhpV6wBkT2-RsvOuIQUg8GTljtArQ?e=0BKehY

The files are as follows:
- 20201106-export: deployments window exported file
- Local-SDA.postman_collection: postman requests
- sdaconfigservice.tar: spring boot server that provides connection to cloud config and provides this service to other applications
- sdacontroller.tar: spring boot app to processes file changes and interacts with DB

To run the dockerised application pls first make sure that you have a running mongodb container on port 2016.
- pull mongoDB container
  command: docker pull mongo
- run mongoDB container on port 27016
  command: docker run -p 27016:27017 -t mongo

sdaconfigservice can be loaded and run by:
- command: docker load -i sdaconfigservice.tar
- command: docker run -p 8182:8182 -t sda/sdaconfigservice

sdacontroller can be loaded by: 
- command: docker load -i sdacontroller.tar
- command: docker run -p 8080:8080 -t sda/sdacontroller

At this point the application should be running and accepting requests on port 8080. The postman requests provided, if executed should provide a valid response although empty for most of them. This is because no CSV file has yet been provided and uploaded. When the CSV file provided is copied to the configured folder, it will be scanned and uploaded by the scheduler
- command: docker cp 20201106-export.csv <sdaController_ContainerName>:/depExports

After the copy command has been executed, the file should be uploaded automatically to the DB and postman requests should now produce a different valid response with details read from the DB

Alternatively if the CSV file on the container is deleted, the data representing the file content is automatically deleted by the scheduler and this can be checked by executing the below docker command and running the postman requests just after
- command: docker exec -it <sdaController_ContainerName> rm /depExports/20201106-export.csv

