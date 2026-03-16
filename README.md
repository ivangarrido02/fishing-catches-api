# Fishing Captures API
A REST API for registering and tracking fishing captures, built with **Java 17**, **Spring Boot 3**, and **PostgreSQL**.
Supports full CRUD operations, dynamic filtering using specifications, and partial updates. Designed to demonstrate clean architecture principles and REST API best practices.

## Table of contents
- [Technologies Used](#technologies-used)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [API Documentation](#api-documentation)
- [API Endpoints](#api-endpoints)
- [Filtering Captures](#filtering-captures)
- [Example Request](#example-request)
- [Error Handling](#error-handling)
- [Project Structure](#project-structure)
- [Testing](#testing)
- [Future Improvements](#future-improvements)
- [License](#license)


##  Technologies Used
- Java 17
- Spring Boot 3
- Spring Data JPA
- MapStruct
- PostgreSQL (production) / H2 (tests)
- OpenAPI / Swagger UI
- JUnit 5 for testing
- Maven
- Lombok

##  Getting Started
### Prerequisites
- JDK 17
- Maven
- PostgreSQL (for production; H2 used for tests)

### Installation

```bash
# 1. Clone the repository
git clone https://github.com/ivangarrido02/fishing-catches-api.git
cd fishing-catches-api

# 2. Build the project
mvn clean install

# 3. Run the application
mvn spring-boot:run
```

##  Configuration

**Production DB:** PostgreSQL configured in application.yaml

**Test DB:** H2 in-memory database used for integration tests

**Port:** Default 8080, can be changed in application.yaml

##  API Documentation

Interactive API documentation is available through **Swagger UI**.

Once the application is running, open:

http://localhost:8080/swagger-ui/index.html

Swagger allows you to:
- Explore all available endpoints
- Test requests directly from the browser
- View request and response schemas

## API Endpoints

| Method | Endpoint | Description |
|--------|---------------------|---------------------------------------------|
| GET    | /api/v1/captures           | List all captures (support pagination & filtering). |
| GET    | /api/v1/captures/{id}      | Get capture by id. |
| POST   | /api/v1/captures           | Create new capture. |
| PUT    | /api/v1/captures/{id}      | Update capture completely. |
| PATCH  | /api/v1/captures/{id}      | Update capture partially. |
| DELETE | /api/v1/captures/{id}      | Delete capture by id. |

## Filtering Captures

The API supports **dynamic filtering using query parameters** to return only the captures that match the specified criteria.

### Single Filter Example

```http
GET /api/v1/captures?name=Sargo&location=El%20Puertito
```

**Supported filters:**

- `name`
- `location`
- `quantity`
- `date`

### Multiple Filters Example

You can combine multiple filters in a single request:

```http
GET /api/v1/captures?name=Sargo&location=El%20Puertito&quantity=2
```

## Example Request

### Create a Capture

**Request**

```http
POST /api/v1/captures
Content-Type: application/json
```

```json
{
  "name": "Sargo",
  "weight": 3.5,
  "location": "El Puertito",
  "date": "2024-01-01",
  "quantity": 2
}
```

**Response — 201 Created**

```json
{
  "id": 1,
  "name": "Sargo",
  "weight": 3.5,
  "location": "El Puertito",
  "date": "2024-01-01",
  "quantity": 2
}
```

### Get a capture by ID

**Request**

```http
GET /api/v1/captures/1
```
**Response — 200 OK**

```json
{
  "id": 1,
  "name": "Sargo",
  "weight": 3.5,
  "location": "El Puertito",
  "date": "2024-01-01",
  "quantity": 2
}
```

### Partial update a capture

**Request**

```http
PATCH /api/v1/captures/1
Content-Type: application/json
```

```json
{
  "weight": 5.0,
  "quantity": 4
}
```

**Response — 200 OK**

```json
{
  "id": 1,
  "name": "Sargo",
  "weight": 5.0,
  "location": "El Puertito",
  "date": "2024-01-01",
  "quantity": 4
}
```

### Delete a capture by ID
**Request**
```http
DELETE /api/v1/captures/1
```
**Response — 204 NO CONTENT**
_No response body_

##  Error Handling

The API uses **centralized exception handling** to return consistent and structured error responses.

### Example

**Request**

```http
GET /api/v1/captures/999
```

**Response — 404 Not Found**

```json
{
    "status": 404,
    "errorCode": "CAPTURE_NOT_FOUND",
    "message": "Capture with id 999 not found",
    "path": "/api/v1/captures/999",
    "timestamp": "2026-03-15T18:17:49Z"
}
```

All API errors follow this structure to ensure consistent client-side handling.

##  Project Structure
```
src/
├─ main/
│  ├─ java/
│  │  └─ com/ivandev/registrocapturas/
│  │     ├─ controller/
│  │     ├─ dto/
│  │     ├─ exception/
│  │     ├─ mapper/
│  │     ├─ model/
│  │     ├─ repository/
│  │     ├─ service/
│  │     └─ specification/
│  └─ resources/
│     └─ application.yaml
└─ test/
   ├─ java/
   │   └─ com/ivandev/registrocapturas/
   │      ├─ controller/
   │      ├─ service/
   │      ├─ mapper/
   │      └─ specification/
   └─ resources/
      └─ application-test.yaml
```

- **Controller:** REST endpoints.
- **DTOs:** Data transfer objects for requests and responses.
- **Mapper:** MapStruct mappers to convert between DTOs and entities.
- **Service:** Business logic and validation.
- **Repository:** JPA repository and specifications for filtering.
- **Specification:** Dynamic filtering logic for queries.
- **Exception:** Centralized error handling.

##  Testing

This project includes **unit tests** and **integration tests** to ensure the correctness of the API, business logic, and database queries.  
Tests are implemented using **JUnit 5**, **Mockito**, and **Spring Boot Test**.

### Running Tests

`$ ./mvnw test`
### Unit Tests
Unit tests verify isolated components using mocks. They focus on:

**Mapper:** Verify DTO ↔ Entity conversions (`CaptureMapper`).

**Service:** Validate business logic, input validation, and exception handling (`CaptureService`).

**Controller:** Ensure REST endpoints return expected responses and handle errors (`CaptureController`).

**Example: Mapper Unit Test**

```java
@Test
void toResponseDTO_validEntity_returnsResponseDTO() {
    // Arrange
    Capture capture = new Capture("Sargo", 3.5, "El Puertito", LocalDate.of(2024, 1, 1), 2);
    capture.setId(1L);

    // Act
    CaptureResponseDTO response = mapper.toDTO(capture);

    // Assert
    assertEquals(capture.getId(), response.getId());
    assertEquals(capture.getName(), response.getName());
    assertEquals(capture.getWeight(), response.getWeight(), 0.0001);
    assertEquals(capture.getLocation(), response.getLocation());
    assertEquals(capture.getDate(), response.getDate());
    assertEquals(capture.getQuantity(), response.getQuantity());
}
```

### Integration Tests

Integration tests verify that multiple layers work together, particularly repository queries and specifications.
They run against an **H2 in-memory database** to simulate real database interactions.

**Specification / Repository:** Tests dynamic filtering and JPA repository logic.

Ensures queries and specifications return the correct results when multiple entities exist in the database.

**Example: Specification Integration Test**

```java
@SpringBootTest
@Transactional
class CaptureSpecificationIntegrationTest {

    @Autowired
    private CaptureRepository repository;
	
	private Capture c1;
	private Capture c2;
	private Capture c3;
	
	@BeforeEach
	void setUp() {
		repository.deleteAll();
		
		c1 = repository.save(new Capture("Sargo", 1.2, "El puertito", LocalDate.of(2024, 1, 1), 2));
		c2 = repository.save(new Capture("Bacalao", 5.0, "La playa", LocalDate.of(2024, 1, 2), 1));
	    c3 = repository.save(new Capture("Sargo", 2.5, "El puertito", LocalDate.of(2024, 1, 3), 4));
	}

   @Test
	void findCaptureByNameLocationAndQuantity_returnsMatchingCaptures() {
	
		// Arrange
		CaptureFilterDTO filter = new CaptureFilterDTO();
		filter.setName("Sargo");
		filter.setLocation("El puertito");
		filter.setQuantity(2);
		
		// Act
		List<Capture> result = repository.findAll(CaptureSpecification.buildCaptureFilter(filter));
		
		// Assert
		assertEquals(1, result.size());
		assertTrue(result.contains(c1));
	}
}
```

## Future Improvements
- Refactor location field from String to a dedicated Location entity
- Add authentication with JWT
- Implement rate limiting for API endpoints
- Dockerize the application

## License

This project is licensed under the MIT License.