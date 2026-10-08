# Expense Tracker API

A RESTful Expense Tracker API built with **Java and Spring Boot** for creating, retrieving, updating, deleting, filtering, and summarizing financial transactions.

The API exposes endpoints for managing transactions and retrieving transaction summaries based on predefined filters.

---

## Features

- Create financial transactions
- Retrieve all transactions
- Filter transactions by category
- Filter transactions by transaction type
- Retrieve a transaction by its transaction ID
- Retrieve transaction summaries
- Update transactions
- Delete transactions
- Request validation using Jakarta Bean Validation
- RESTful API architecture
- OpenAPI/Swagger documentation support

---

## Technology Stack

- **Java 21**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **PostgreSQL**
- **Jakarta Bean Validation**
- **Lombok**
- **MapStruct**
- **Maven**
- **OpenAPI / Swagger**

---

## Project Structure

The application follows a layered architecture:

```text
src/main/java/com/elegax/expenseTracker
│
├── controllers
│   └── TransactionController
│
├── services
│   └── TransactionService
│
├── dto
│   ├── SummaryResponse
│   ├── TransactionRequest
│   ├── TransactionResponse
│   └── TransactionUpdateRequest
│
├── entity
│   ├── Category
│   ├── TransactionType
│   └── SummaryFilter
│
└── ...
```

### Architecture

```text
Client
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
Database
```

The controller is responsible for handling HTTP requests and returning HTTP responses, while business logic is delegated to the service layer.

---

# API Base URL

When running locally, the API is available at:

```text
http://localhost:8080/api
```

---

# API Endpoints

## Transactions

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/transactions` | Retrieve all transactions or filter transactions |
| GET | `/api/transactions/{transactionId}` | Retrieve a transaction by ID |
| POST | `/api/transactions/new` | Create a new transaction |
| PATCH | `/api/transactions/{transactionId}` | Update a transaction |
| DELETE | `/api/transactions/{transactionId}` | Delete a transaction |

---

# 1. Get Transactions

### `GET /api/transactions`

Retrieves transactions.

The endpoint supports optional filtering by:

- Category
- Transaction type

If no filters are supplied, the endpoint returns all transactions.

### Request

```http
GET /api/transactions
```

### Filter by Category

```http
GET /api/transactions?category=FOOD
```

### Filter by Transaction Type

```http
GET /api/transactions?transactionType=INCOME
```

### Combine Filters

```http
GET /api/transactions?category=FOOD&transactionType=EXPENSE
```

### Query Parameters

| Parameter | Required | Description | Example |
|---|---|---|---|
| `category` | No | Filters transactions by category | `FOOD` |
| `transactionType` | No | Filters transactions by transaction type | `INCOME` |

### Response

```http
200 OK
```

The endpoint returns a list of `TransactionResponse` objects.

Example structure:

```json
[
  {
    "id": "transaction-id",
    "amount": 5000,
    "description": "Restaurant",
    "category": "FOOD",
    "transactionType": "EXPENSE"
  }
]
```

> The exact response fields depend on the implementation of `TransactionResponse`.

---

# 2. Get Transaction by ID

### `GET /api/transactions/{transactionId}`

Retrieves a specific transaction using its transaction ID.

### Request

```http
GET /api/transactions/{transactionId}
```

Example:

```http
GET /api/transactions/txn-001
```

### Path Parameter

| Parameter | Required | Description |
|---|---|---|
| `transactionId` | Yes | Unique identifier of the transaction |

### Response

```http
200 OK
```

Returns a `TransactionResponse`.

Example:

```json
{
  "id": "txn-001",
  "amount": 5000,
  "description": "Restaurant",
  "category": "FOOD",
  "transactionType": "EXPENSE"
}
```

---

# 3. Get Transaction Summary

### `GET /api/summary`

Retrieves a transaction summary based on the supplied summary filter.

### Request

```http
GET /api/summary?filterBy=MONTH
```

### Query Parameter

| Parameter | Required | Description |
|---|---|---|
| `filterBy` | Yes | Determines the summary filter to apply |

The available values depend on the `SummaryFilter` enum defined by the application.

### Response

```http
200 OK
```

Returns a `SummaryResponse`.

---

# 4. Create Transaction

### `POST /api/transactions/new`

Creates a new transaction.

The request body is validated using Jakarta Bean Validation.

### Request

```http
POST /api/transactions/new
Content-Type: application/json
```

Example request:

```json
{
  "amount": 5000,
  "description": "Restaurant",
  "category": "FOOD",
  "transactionType": "EXPENSE"
}
```

> The exact request fields depend on the `TransactionRequest` DTO.

### Response

```http
201 Created
```

The endpoint does not return a response body.

---

# 5. Update Transaction

### `PATCH /api/transactions/{transactionId}`

Updates an existing transaction.

The transaction is identified using its transaction ID.

### Request

```http
PATCH /api/transactions/{transactionId}
Content-Type: application/json
```

Example:

```http
PATCH /api/transactions/txn-001
```

Example request body:

```json
{
  "amount": 7500,
  "description": "Updated restaurant expense"
}
```

> The exact fields available for updating depend on `TransactionUpdateRequest`.

### Path Parameter

| Parameter | Required | Description |
|---|---|---|
| `transactionId` | Yes | Unique identifier of the transaction |

### Response

```http
204 No Content
```

A successful update does not return a response body.

---

# 6. Delete Transaction

### `DELETE /api/transactions/{transactionId}`

Deletes a transaction using its transaction ID.

### Request

```http
DELETE /api/transactions/{transactionId}
```

Example:

```http
DELETE /api/transactions/txn-001
```

### Path Parameter

| Parameter | Required | Description |
|---|---|---|
| `transactionId` | Yes | Unique identifier of the transaction |

### Successful Response

```http
204 No Content
```

### Transaction Not Found

```http
404 Not Found
```

---

# HTTP Status Codes

The API uses standard HTTP status codes to communicate the result of requests.

| Status Code | Meaning |
|---|---|
| `200 OK` | Request completed successfully |
| `201 Created` | Transaction successfully created |
| `204 No Content` | Request succeeded with no response body |
| `400 Bad Request` | Request validation or input error |
| `404 Not Found` | Requested transaction could not be found |

---

# Request Validation

The API uses Jakarta Bean Validation.

Request DTOs are validated using:

```java
@Valid
```

For example:

```java
@PostMapping("/transactions/new")
public ResponseEntity<Void> createTransaction(
        @Valid @RequestBody TransactionRequest transactionRequest
) {
    transactionService.createTransaction(transactionRequest);

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .build();
}
```

Validation rules are defined inside the request DTOs.

For example:

```java
@NotNull
@Positive
private BigDecimal amount;
```

Invalid requests should be rejected before the request reaches the business logic.

---

# Filtering Transactions

Transactions can be filtered using query parameters.

### All transactions

```http
GET /api/transactions
```

### By category

```http
GET /api/transactions?category=FOOD
```

### By transaction type

```http
GET /api/transactions?transactionType=EXPENSE
```

### By both category and transaction type

```http
GET /api/transactions?category=FOOD&transactionType=EXPENSE
```

This allows the same collection endpoint to support different filtering requirements without creating separate endpoints for every combination.

---

# Swagger / OpenAPI Documentation

The API uses OpenAPI annotations to document its endpoints.

Examples include:

```java
@Tag
@Operation
@ApiResponse
@ApiResponses
@Parameter
```

When the application is running, Swagger UI can be used to explore and test the API.

Typical Springdoc URLs are:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI specification:

```text
http://localhost:8080/v3/api-docs
```

---

# Running the Application

## Prerequisites

Make sure the following are installed:

- Java 21 or later
- Maven
- PostgreSQL

---

## Clone the Repository

```bash
git clone https://github.com/your-username/expense-tracker-api.git
```

Navigate into the project:

```bash
cd expense-tracker-api
```

---

# API Design

The API follows a REST-oriented design where:

- `GET` is used for retrieving resources.
- `POST` is used for creating resources.
- `PATCH` is used for partially updating resources.
- `DELETE` is used for deleting resources.
- Path variables identify specific resources.
- Query parameters are used for filtering and summary options.
- Request DTOs are used to receive client data.
- Response DTOs are used to return API data.

# Author

**Elegax**

Expense Tracker REST API built with Java and Spring Boot.