RESTful BigData Analytics Service
======================

Steps:

1. Clone the project.

2. $ mvn clean package

3. $ java -jar target/bigdata-0.0.1-SNAPSHOT.jar server config/dev_config.yml 

# How to run this Java process forever
$ nohup ./bin/dev.sh 0<&- &> /tmp/app.log &

Service endpoint: http://localhost:9080/bigdata/
                  http://localhost:9080/bigdata/dashboard




