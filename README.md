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

---

## **Technology Stack**

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- MySQL
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
security:
  api-key: 3fa85f64-5717-4562-b3fc-2c963f66afa6-DEVKEY-92A7D1
```
You can change the key and store it in environment variables for security.

---

## **4. Build and run the application**
```bash
  mvn clean install
  mvn spring-boot:run
```
- The application will start on http://localhost:8080.

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


## **Swagger / OpenAPI Documentation**
Swagger UI is available at:
```bash
  http://localhost:8080/swagger-ui/index.html
```
- Click Authorize in Swagger UI
- Enter your API key under X-API-KEY
- All requests from Swagger will include the API key automatically

This allows you to test endpoints directly from the browser.

## **Testing**

- Unit tests are included for service and converters
- DTO builders are used to easily generate test data
- Run all tests:
```bash
  mvn test
```

## **Project Structure**

```bash
    src/main/java/com/naveen/devices/
    ├── config/          # Swagger, Security, API Key filter
    ├── controller/      # REST Controllers
    ├── domain/          # JPA Entities
    ├── dto/             # Request/Response DTOs
    ├── converter/       # Service to convert
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

## **Future Improvements**
- Add caching for frequent queries
- Add pagination for /api/devices endpoint
- Implement authentication & authorization

  
