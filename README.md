# Backend Developer Assignment

The goal of this assignment is to implement RESTful API webservice that will be responsible for
managing and storing in database simple notes (without the UI part).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

* Maven [3.3.9 or above] - https://maven.apache.org/
* Apache Tomcat [9.0.8 or above] - http://tomcat.apache.org/
* PostgreSQL [9.6.9]

Detailed informations about installation and configurations are provided at developers' site.

## Technology Stack

* Spring Boot [2.0.2]
* JPA
* Hibernate
* PostgreSQL
* H2 (for testing purposes)
* Maven [3.3.9]
* Java 8

### Build 

A step by step instruction [on Windows 7]
* Install PostgreSQL [9.6.9] (set `user: postgres` and `password: postgres`during installation, otherwise it will not work!
* Navigate project directory in Windows command prompt (cmd)
* Use Maven to build project with command in cmd
  ```
  mvn install
  ```
* Copy 2 service files from project directory

 ```
 \RESTful-API-webservice-master\webservice\target\web-service-RELEASE.war
 ```
 ````
 \RESTful-API-webservice-master\additional\target\additional-RELEASE.war
 ````
Navigate main directory of previously installed Apache Tomcat and paste these files into webapps directory

(example:``C:\apache-tomcat-9.0.8\webapps\``)
 * Navigate bin folder in Apache Tomcat main directory 
(example:``C:\apache-tomcat-9.0.8\bin``)
 * Run `` startup.bat `` file
 * Apache Tomcat should start webservices, you can check that by visiting http://localhost:8080/ in your browser
 

## Example Usage
## CRUD commands
### Create note
Request
`http://localhost:8080/webservice-RELEASE/notes`
```
POST /webservice-RELEASE/notes/ HTTP/1.1
Host: localhost:8080
Content-Type: application/json
{
    "title": "Notes title",
    "content": "Notes content"
}
```
Response `Status: 204 No Content`
``location →http://localhost:8080/webservice-RELEASE/notes/17``

### Read note
Request
`http://localhost:8080/webservice-RELEASE/notes/{id}`
```
GET /webservice-RELEASE/notes/4 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```
Response
`Status: 201 Created`
```
{
    "id": 4,
    "title": "New title",
    "content": "New content",
    "creationDate": "2012-05-20T12:49:46.458",
    "modificationDate": "2012-05-20T12:49:46.458",
    "archived": true
}
```
### Update note
Request
`http://localhost:8080/webservice-RELEASE/notes/{id}`
```
PUT /webservice-RELEASE/notes/4 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
{
    "title": "tittle",
    "content": "imie",
    "archived": true
}
```
Response `Status: 204 No Content`

### Delete note
Request
`http://localhost:8080/webservice-RELEASE/notes/{id}`
```
DELETE /webservice-RELEASE/notes/4 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```
Response `Status: 200 OK`

## Functional commands
### Returns notes that were not updated for more than a month
Request
`http://localhost:8080/additional-RELEASE/month`
```
GET /additional-RELEASE/month HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```
Response `Status: 200 OK`
```
[
    {
        "id": 11,
        "title": "Title",
        "content": "Content 2",
        "creationDate": "2012-10-22T11:13:28",
        "modificationDate": "2012-10-24T11:13:28",
        "archived": true
    }
]
```
### Returns all the notes with sorting and pagination support
Request
`http://localhost:8080/additional-RELEASE/all?sort=id,desc&page=3&size=1`
```
GET /additional-RELEASE/all?sort=id,desc&amp;page=3&amp;size=1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```
Response `Status: 200 OK`
```
[
    {
        "id": 14,
        "title": "how to become IT intern",
        "content": ":)",
        "creationDate": "2018-05-23T16:26:48.518",
        "modificationDate": "2018-05-23T16:26:48.518",
        "archived": false
    }
]
```
### Archives all the notes older than given date
Request
`http://localhost:8080/additional-RELEASE/archive`
```
PUT /additional-RELEASE/archive HTTP/1.1
Host: localhost:8080
Content-Type: application/json
{
    "title": "2018-05-23T19:29:32"
}
```
Response `Status: 204 No content`
### Returns all the archived notes
Request
`http://localhost:8080/additional-RELEASE/archive`
```
GET /additional-RELEASE/archive HTTP/1.1
Host: localhost:8080
Content-Type: application/json
```
Response
`Status: 200 OK`
```
[
    {
        "id": 9,
        "title": "title",
        "content": "content",
        "creationDate": "2018-05-22T14:50:15.321",
        "modificationDate": "2018-05-22T14:50:15.321",
        "archived": true
    }
]
```
## Author

* **Michał Koziara** 

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
