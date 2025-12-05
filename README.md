# Devices API

A **Spring Boot 3.5 / Java 21 REST API** for managing devices, with **CRUD operations**, **Liquibase database migrations**, **MySQL persistence**, **DTO mapping with Builder**, and **DTO/Entity builders with Lombok**.

---

## **Key Features**

- Create, update, fetch, and delete devices
- Fetch devices by brand or state
- Validation rules:
    - Cannot update name/brand if device is **in use**
    - Cannot delete a device if it is **in use**
- Automatic auditing for `creationTime` and `updateTime`
- Fully containerized with Docker for app and MySQL
- Unit-testable DTOs with builders
- **API Key security** to protect endpoints
- **Swagger UI / OpenAPI** documentation
- Integration tests with H2 in-memory database
- SonarQube code quality and coverage analysis

---

## **Technology Stack**

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- MySQL
- H2 (for integration tests)
- Liquibase
- Lombok
- Springdoc OpenAPI / Swagger
- Spring Security (API Key)
- Maven 3.9+
- Docker

---

## **Prerequisites**

- Java 21+
- Maven 3.9+
- Docker & Docker Compose
- Git
- SonarQube (local or remote instance)

---

## **Setup & Run**

### **1. Clone the repository**

```bash
    git clone https://github.com/nangunoorinaveenkumar-hub/devices-api.git
    cd devices-api
```
## **2. Configure MySQL (Docker)**
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: devices-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: devicesdb
      MYSQL_USER: user
      MYSQL_PASSWORD: password
    ports:
      - "3306:3306"
    volumes:
      - devices-mysql-data:/var/lib/mysql

volumes:
  devices-mysql-data:

```
### **Run:**
```bash
  docker-compose up -d
```

## **3 Set the API Key**
The application requires an API key for all requests (except Swagger UI):
```yaml
api:
  key: 3fa85f64-5717-4562-b3fc-2c963f66afa6-DEVKEY-92A7D1
```
You can change the key and store it in environment variables for security.

---

## **4. Build and run the application**
```bash
  mvn clean install
  mvn spring-boot:run
```
- The application will start on http://localhost:8080.

## **API Access / Swagger UI**
- Devices API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- All requests require the X-API-KEY header
- Authorize once in Swagger UI to include API key in all requests

## **API Endpoints**
Method	                             URL	                    Description

---
- POST	                          /api/devices	                Create a new device
- GET	                          /api/devices	                Fetch all devices
- GET	                          /api/devices/{id}	            Fetch a device by ID
- GET	                          /api/devices/brand/{brand}	Fetch devices by brand
- GET	                          /api/devices/state/{state}	Fetch devices by state
- PUT	                          /api/devices/{id}	            Fully update a device
- PATCH	                          /api/devices/{id}	            Partially update a device
- DELETE	                      /api/devices/{id}	            Delete a device

All endpoints require the X-API-KEY header with the value set in your configuration.

## **Testing**

### **1. Unit Tests**

- Unit tests are included for service and converters
- DTO builders are used to easily generate test data
- Run all tests:
```bash
  mvn test
```
### **2. Integration Tests with H2**

- Integration tests use H2 in-memory database.
- Ensures isolation and fast execution without requiring Docker.
- Configured in src/test/resources/application-test.yml:
```yaml
spring:
  datasource:
    url: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
    driverClassName: org.h2.Driver
    username: sa
    password:
  jpa:
    hibernate:
      ddl-auto: update
api:
  key: test-api-key
```
- API key is dynamically read from test YAML.
- Example test:
```java
@Autowired
private MockMvc mockMvc;

@Value("${api.key}")
private String apiKey;

private RequestPostProcessor apiKeyHeader() {
    return request -> {
        request.addHeader("X-API-KEY", apiKey);
        return request;
    };
}
```
- All endpoints are covered in integration tests using MockMvc.

## **SonarQube Integration**
### **1.Maven Plugin**
```xml
<plugin>
    <groupId>org.sonarsource.scanner.maven</groupId>
    <artifactId>sonar-maven-plugin</artifactId>
    <version>5.1.0.4751</version>
</plugin>
```
### **2.Sonar Properties**
```properties
sonar.projectKey=devices-api
sonar.host.url=http://localhost:9000
sonar.login=<your-generated-token>
sonar.java.coveragePlugin=jacoco
sonar.sources=src/main/java
sonar.tests=src/test/java
sonar.java.test.binaries=target/classes
sonar.junit.reportPaths=target/surefire-reports
sonar.jacoco.reportPaths=target/jacoco.exec
```
### **3.Run SonarQube Analysis**
```bash
    mvn clean test jacoco:report sonar:sonar \
      -Dsonar.projectKey=devices-api \
      -Dsonar.host.url=http://localhost:9000 \
      -Dsonar.login=<your-token> \
      -Dspring.profiles.active=test
```
- Ensures tests are run and coverage is calculated.
- Integration tests using H2 contribute to overall coverage.
- Recommended coverage goal: ≥ 80% with 0 code smells and 0 duplications.

### **4. Sonar Highlights**

- Shows coverage, duplications, maintainability, and security hotspots.
- Integration and unit tests contribute to the overall metrics.
- Current metrics of device-api:
    - Code Coverage: 84%
    - Code Smells: 0
    - Duplications: 0%
    - Bugs: 0

## **Project Structure**

```bash
    src/main/java/com/naveen/devices/
    ├── config/          # Swagger, Security, API Key filter
    ├── controller/      # REST Controllers
    ├── domain/          # JPA Entities
    ├── dto/             # Request/Response DTOs
    ├── converter/       # Service to convert DTO ↔ Entity
    ├── repository/      # Spring Data Repositories
    ├── service/         # Business Logic
```

## **Liquibase**
- Database changes are managed with Liquibase
- Master changelog: src/main/resources/db/changelog/db.changelog-master.yml
- Initial table: devices

## **Security**
- All endpoints are protected with a custom API key
- The key must be sent in the header:
```bash
  X-API-KEY: 3fa85f64-5717-4562-b3fc-2c963f66afa6-DEVKEY-92A7D1
```
- Invalid or missing API key returns HTTP 401 Unauthorized

## **Deployment / Running the Application**
```markdown
### **Using Docker Compose (Production)**

The API and MySQL can be started together using Docker Compose:
```

```bash
  docker-compose --profile prod up --build -d
```
- This will start:
- MySQL database (mysql:8.0) on port 3306
   - Devices API on port 8080
- API key is automatically set from environment variables or application-prod.yml.

### **Using Docker Compose (Local Development / Integration Tests)**
```bash
   docker-compose --profile dev up --build -d
```
- Starts MySQL or H2 in-memory database for local development.
- Spring Boot test profile will be active automatically for integration tests.

### **Running Locally Without Docker**
```bash
   # Build the project
mvn clean install

# Run with production profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```
- Ensure application-prod.yml is correctly configured to point to your local MySQL database.
- API key must be set in application-prod.yml or environment variable:
```bash
  export API_KEY=3fa85f64-5717-4562-b3fc-2c963f66afa6-DEVKEY-92A7D1
```

## **Known Limitations / Future Improvements**
- Currently, multiple devices with the same `name` and `brand` can be created.
- In a multi-user setup, `(user_id, name, brand)` uniqueness could be enforced to avoid duplicates per user.
- Pagination and caching for large datasets.
- Optional authentication/authorization enhancements beyond API key.
- Increase test coverage for edge cases and integration tests
- CI/CD integration with SonarQube for automated quality checks

  
