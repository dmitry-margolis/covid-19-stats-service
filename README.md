# Java Developer Assessment
Write an application that calculates the correlation coefficient between the percentage of people that died and got vaccinated of COVID-19 given a continent or all available countries using the API as described on: https://github.com/M-Media-Group/Covid-19-API.

### Initial requirements
- Write in Java or any other language targeting the JVM
- Write production-ready code
- Free to use any libraries and/or frameworks
- Document how to run the application
- Publish the source code into GitHub (using your own personal account) and share it with us

### Technology stack
Java 11, Spring Boot 2.6.X, OpenFeign, Ehcache, commons-math3, MapStruct

### Build + Run
From cmd from project root:
- Build: ```gradlew build```
  If there are problems with build, prebuild jar from binaries folder can be used.
- Run: ```java -jar build\libs\covid-19-stats-service-1.0.jar```

### Endpoints
Postman collection file: binaries/covid-19-stats-service.postman_collection
- All: http://localhost:8085/api/v1/analysis/vaccines-to-deaths-pcc
- Continent: http://localhost:8085/api/v1/analysis/vaccines-to-deaths-pcc?continent=Europe

### Documentation
- SwaggerUI - http://localhost:8085/swagger-ui/index.html
- OpenAPI - http://localhost:8080/v3/api-docs

