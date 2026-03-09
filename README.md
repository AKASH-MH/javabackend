# Job Portal REST API

A Spring Boot REST API for managing **Job Openings**, **Candidates**, and **Applications**.

## Tech Stack

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- Bean Validation (Jakarta)

## Prerequisites

| Tool       | Version |
|------------|---------|
| Java JDK   | 17+     |
| PostgreSQL | 14+     |
| Maven      | 3.8+ (or use the included wrapper `mvnw`) |

## Quick Start

### 1. Create the Database

```sql
CREATE DATABASE jobportal;
```

Or run the provided SQL in `src/main/resources/schema.sql` against your PostgreSQL instance.

### 2. Configure Connection

Edit `src/main/resources/application.properties` if your PostgreSQL settings differ from the defaults:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jobportal
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### 3. Build

```bash
./mvnw clean package -DskipTests
```

### 4. Run

```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

---

## API Endpoints

### Job Openings — `/api/v1/jobs`

| Method   | Path               | Description              |
|----------|-------------------|--------------------------|
| `POST`   | `/api/v1/jobs`     | Create a job opening     |
| `GET`    | `/api/v1/jobs`     | List all (paginated)     |
| `GET`    | `/api/v1/jobs/{id}` | Get by ID               |
| `PUT`    | `/api/v1/jobs/{id}` | Update                  |
| `DELETE` | `/api/v1/jobs/{id}` | Delete                  |

**Query params:** `?status=OPEN`, `?page=0&size=10&sort=title,asc`

### Candidates — `/api/v1/candidates`

| Method   | Path                      | Description          |
|----------|--------------------------|----------------------|
| `POST`   | `/api/v1/candidates`      | Register candidate   |
| `GET`    | `/api/v1/candidates`      | List all (paginated) |
| `GET`    | `/api/v1/candidates/{id}` | Get by ID            |
| `PUT`    | `/api/v1/candidates/{id}` | Update               |
| `DELETE` | `/api/v1/candidates/{id}` | Delete               |

### Applications — `/api/v1/applications`

| Method   | Path                                         | Description             |
|----------|----------------------------------------------|-------------------------|
| `POST`   | `/api/v1/applications`                       | Apply to a job          |
| `GET`    | `/api/v1/applications`                       | List all (paginated)    |
| `GET`    | `/api/v1/applications/{id}`                  | Get by ID               |
| `GET`    | `/api/v1/applications/candidate/{candidateId}` | List by candidate     |
| `GET`    | `/api/v1/applications/job/{jobId}`           | List by job             |
| `PUT`    | `/api/v1/applications/{id}/status`           | Update status           |
| `DELETE` | `/api/v1/applications/{id}`                  | Withdraw / delete       |

---

## Sample Requests

### Create a Job Opening

```bash
curl -X POST http://localhost:8080/api/v1/jobs \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Backend Engineer",
    "description": "Build REST APIs with Spring Boot",
    "location": "Remote",
    "department": "Engineering",
    "employmentType": "FULL_TIME"
  }'
```

### Register a Candidate

```bash
curl -X POST http://localhost:8080/api/v1/candidates \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "jane@example.com",
    "phone": "1234567890",
    "skills": "Java, Spring Boot, PostgreSQL"
  }'
```

### Apply to a Job

```bash
curl -X POST http://localhost:8080/api/v1/applications \
  -H "Content-Type: application/json" \
  -d '{
    "candidateId": 1,
    "jobOpeningId": 1
  }'
```

### Update Application Status

```bash
curl -X PUT http://localhost:8080/api/v1/applications/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "INTERVIEW"}'
```

---

## Project Structure

```
src/main/java/com/jobportal/
├── JobPortalApplication.java      # Entry point
├── controller/                    # REST controllers
│   ├── JobOpeningController.java
│   ├── CandidateController.java
│   └── ApplicationController.java
├── service/                       # Business logic
│   ├── JobOpeningService.java
│   ├── CandidateService.java
│   └── ApplicationService.java
├── repository/                    # Spring Data JPA repos
│   ├── JobOpeningRepository.java
│   ├── CandidateRepository.java
│   └── ApplicationRepository.java
├── entity/                        # JPA entities
│   ├── JobOpening.java
│   ├── Candidate.java
│   └── Application.java
├── dto/                           # Data transfer objects
│   ├── JobOpeningDTO.java
│   ├── CandidateDTO.java
│   ├── ApplicationDTO.java
│   └── StatusUpdateDTO.java
└── exception/                     # Error handling
    ├── ResourceNotFoundException.java
    ├── DuplicateResourceException.java
    ├── ErrorResponse.java
    └── GlobalExceptionHandler.java
```

---

## Status / Enum Values

| Entity      | Field            | Values                                                  |
|-------------|------------------|---------------------------------------------------------|
| JobOpening  | `status`         | `OPEN`, `CLOSED`, `DRAFT`                               |
| JobOpening  | `employmentType` | `FULL_TIME`, `PART_TIME`, `CONTRACT`, `INTERNSHIP`      |
| Application | `status`         | `APPLIED`, `SCREENING`, `INTERVIEW`, `OFFERED`, `REJECTED`, `WITHDRAWN` |
