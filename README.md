# Devices API

A **Spring Boot 3.5 / Java 21 REST API** for managing devices, with **CRUD operations**, **Liquibase database migrations**, **MySQL persistence**, **DTO mapping with MapStruct**, and **DTO/Entity builders with Lombok**.

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

---

## **Technology Stack**

- Java 21
- Spring Boot 3.5
- Spring Data JPA
- MySQL
- Liquibase
- MapStruct
- Lombok
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

## **3. Build and run the application**
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

## **Testing**

- Unit tests are included for service and mapper layers
- DTO builders are used to easily generate test data
- Run all tests:
```bash
  mvn test
```

## **Project Structure**

```bash
    src/main/java/com/naveen/devices/
    ├── controller/       # REST Controllers
    ├── domain/           # JPA Entities
    ├── dto/              # Request/Response DTOs
    ├── mapper/           # MapStruct Mappers
    ├── repository/       # Spring Data Repositories
    ├── service/          # Business Logic
```

## **Liquibase**
- Database changes are managed with Liquibase
- Master changelog: src/main/resources/db/changelog/db.changelog-master.yml
- Initial table: devices

## **Future Improvements**
- Add caching for frequent queries
- Add pagination for /api/devices endpoint
- Add Swagger/OpenAPI documentation
- Implement authentication & authorization

  
