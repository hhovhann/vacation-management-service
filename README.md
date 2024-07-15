# vacation-management-service
Vacation Management Service Spring Boot Demo Application

# Software Document
Vacation Management Service Rest API service project for Spring Boot

## Software Environment

- Java 21
- MySQL database
- Flyway migration
- Spring Boot 3.3.1
- Maven 3.9.5
- The Room Java Library 2.1.0

## Software Structure
- design
- postman
- scripts
- src
    - main
        - feature_name(vacation)
            - controller
            - dto
            - entity
            - enums
            - exception
            - mapper
            - repository
            - service
    - test

# How to run from the terminal and the Intellij IDEA
- Maven Build: `mvn clean spring-boot:run`
- Edit Configuration: Run Main class `com.hhovhann.VacationManagementServiceApplication`

## Software Behaviour Testing
System designed as a Spring Boot Web Application. Provides Rest API with following endpoints:

* IMPORTANT: For manual testing the API you can use
    * Postman collection `src/main/java/com/hhovhann/postman/Vacation Management Service.postman_collection.json`, first please create the list of employees and then using the worker and manager id's creates corresponding list of vacations or one vacation
    * Swagger UI `http://localhost:8080/swagger-ui/index.html`

## Software Setup and Run: Docker containers
- Create .env file from the root project with
  ```
    MYSQL_DATABASE=vacation-management-service-db
    MYSQL_USER=api-user
    MYSQL_PASSWORD=api-password
    MYSQL_ROOT_PASSWORD=api-password
    MYSQL_LOCAL_PORT=13306
    MYSQL_DOCKER_PORT=3306
    SPRING_APP_LOCAL_PORT=8888
    SPRING_APP_DOCKER_PORT=8080
  ```
- From root directory start containers with `docker-compose up`
- To stop the containers run `docker compose down`

## Software Setup and Run: Local Application
- Download and install [Docker Desktop](https://www.docker.com/products/docker-desktop/) if you not have it installed in your machine
- Depends on which database we are going to use, should run:
    - local MySql ``docker run -p 3306:3306 --name vacation-management-service-db -e MYSQL_DATABASE=vacation-management-service-db -e MYSQL_ROOT_PASSWORD=root  -e MYSQL_USER=api-user -e MYSQL_PASSWORD=api-password -d mysql:latest```
- Run application with bach command from project root ./scripts/run.sh
- Run the application from the IDEA itself

## Software testing with custom data initialization
- Open API Docs http://localhost:8080/v3/api-docs
- Swagger UI http://localhost:8080/swagger-ui/index.html

## Software Design and Diagram
Please check the [Vacation Management Service Design](design/vacation-management-service-draft-design-flow.drawio)

# Nice to have in future versions
- Test coverage - unit, integration (with mysql test containers), jmetter
- ElasticSearch/OpenSearch support for search functionality
