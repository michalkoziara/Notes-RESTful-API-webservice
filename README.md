# Backend Developer Assignment

The goal of this assignment is to implement RESTful API webservice that will be responsible for
managing and storing in database simple notes (without the UI part).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* Maven [3.3.9 or above] - https://maven.apache.org/
* Apache Tomcat [9.0.8 or above] - http://tomcat.apache.org/
* PostgreSQL

Detailed informations about installation and configurations are provided at developers' site.

### Build 

A step by step instruction [on Windows 7]
* Navigate project directory in Windows command prompt (cmd)
* Use Maven to build project with command in cmd
  ```
  mvn install
  ```
* Copy 2 service files from project directory

 ```
 RESTful-API-webservice-master\webservice\target\web-service-RELEASE.war
 ```
 ````
 RESTful-API-webservice-master\additional\target\additional-RELEASE.war
 ````
Navigate main directory of previously installed Apache Tomcat and paste these files into webapps directory

(example:``C:\apache-tomcat-9.0.8\webapps\``)
 * Navigate bin folder in Apache Tomcat main directory 
(example:``C:\apache-tomcat-9.0.8\bin``)
 * Run `` startup.bat `` file
 * Apache Tomcat should start webservices

## Example Usage

```

```

## Technology Stack

* Spring Boot [2.0.2]
* JPA
* Hibernate
* PostgreSQL
* H2 (for testing purposes)
* Maven [3.3.9]
* Java 8

## Author

* **Michał Koziara** 

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
