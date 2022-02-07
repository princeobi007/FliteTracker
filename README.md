# FliteTracker Getting Started 

This application is built using Spring boot Command line runner. 
It is a maven application.

To run follow the following steps:
1) Run mvn clean install package -DskipTests to install dependencies and package .jar
2) copy FliteTrakr-0.0.1-SNAPSHOT.jar from /target directory of the project and place in new directory
3) place *.txt file in same directory
4) Run  java -jar FliteTrakr-0.0.1-SNAPSHOT.jar *.txt 

#### PLEASE NOTE * REPRESENTS THE FILE NAME
If your input file is input.txt, resulting command is  java -jar FliteTrakr-0.0.1-SNAPSHOT.jar input.txt

### Dependency List
For further reference, please consider the following sections:

* Spring-Boot-Starter for managing dependency
* Spring-boot-devtools for managing application life cycle during development
* lombok for pojo management
* Spring-boot-test for unit testing

### Final Thoughts
 I believe the answers to question 7, 8f, 9 as defined in the spec are inaccurate.

7) How many connections with maximum 3 stops exists between NUE and FRA? should be 1 as NUE - FRA is a direct connection
8) How many connections with exactly 1 stop exists between LHR and AMS? should be 0 because you can only get to AMS for LHR using [LHR,NUE,FRA,AMS]
9) Find all connections from NUE to LHR below 170Euros! should be NUE-FRA-LHR-70 only. Not sure why i would get to the destination and repeat the trip to meet a criteria. NUE-FRA-LHR-NUE-FRA-LHR-163
