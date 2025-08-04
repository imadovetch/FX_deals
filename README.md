# Bloomberg FX Deals Data Warehouse - Deal Importer API

A robust and scalable REST API for importing FX deal data into Bloomberg's data warehouse system. Built for high-volume financial data processing with comprehensive validation, error handling, and clean architecture using Aspect-Oriented Programming (AOP).

## 🚀 Features

- **Financial Data Processing**: Specialized for FX deals import and validation
- **Clean Architecture**: Separation of concerns using AOP for cross-cutting functionality
- **Comprehensive Validation**: Multi-layer validation with detailed error responses
- **Duplicate Detection**: Intelligent handling of duplicate deals
- **Data Warehouse Ready**: Optimized for Bloomberg's analytical requirements
- **Production Ready**: Comprehensive logging, error handling, and monitoring

## 📁 Project Structure

```
FX_deals/
├── src/
│   ├── main/java/org/bloomberg/fx_deals/
│   │   ├── aspect/           # AOP cross-cutting concerns
│   │   ├── controller/       # REST API endpoints
│   │   ├── Corevalidation/   # Business validation logic
│   │   ├── Exceptions/       # Global exception handling
│   │   ├── Helpers/          # Utility classes
│   │   ├── Mapper/           # DTO-Entity mapping
│   │   ├── Model/            # DTOs and Entities
│   │   ├── repository/       # Data access layer
│   │   ├── service/          # Business logic
│   │   ├── context/          # Thread-local context
│   │   ├── security/         # Security configuration
│   │   └── config/           # Swagger and app configurations
│   └── test/                 # Comprehensive test suite
├── docker-compose.yml        # Complete deployment stack
├── Dockerfile               # Multi-stage build
├── sample-data/             # Test data files
├── scripts/                 # Deployment and testing scripts
└── Makefile                 # Build automation
```

## 🧠 AOP for Business Separation

We use Spring AOP to intercept exceptions and log them cleanly to the console. This keeps the business logic focused only on what it needs to do and removes repetitive try-catch blocks.

## 🛠️ Tech Stack

- **Framework**: Spring Boot 3.x
- **AOP**: Spring AOP for cross-cutting concerns
- **Validation**: Hibernate Validator with custom annotations
- **Logging**: SLF4J + Logback
- **Database**: JPA/Hibernate
- **Documentation**: Swagger/OpenAPI 3
- **Containerization**: Docker & Docker Compose
- **Build Tools**: Maven
- **Code Quality**: Lombok for boilerplate reduction

### Switching to Custom Annotations

You can easily switch to using custom annotations to leverage aspects in a simple way. The system is designed with the following principles:

- **No Transactional Operations**: If anything fails, it doesn't affect other operations
- **Request Body Validation First**: Only requires a valid request body as the initial requirement
- **Aspect-Driven Processing**: Use custom annotations to trigger AOP behavior


## 📊 API Response Types

### ✅ Successful Import
```json
{
    "duplicateDeals": [],
    "failedDeals": [],
    "message": "All deals imported successfully. | Success: 2 | Failed: 0 | Duplicates: 0",
    "successfulDeals": [
        "DEAL0031",
        "66666"
    ]
}
```

### ⚠️ Duplicate Deals Detected
```json
{
    "duplicateDeals": [
        "DEAL0031",
        "66666"
    ],
    "failedDeals": [],
    "message": "All deals were duplicates and skipped. | Success: 0 | Failed: 0 | Duplicates: 2",
    "successfulDeals": []
}
```

### ❌ Validation Error
```json
{
    "errors": {
        "0": [
            {
                "property": "dealUniqueId",
                "message": "Deal Unique Id is required"
            }
        ]
    },
    "status": 400,
    "type": "Validation Error",
    "timestamp": "2025-08-04T03:31:06.980127Z"
}
```

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Docker & Docker Compose
- Maven 3.6+

### Option 1: Using Makefile (Recommended)
```bash
run : make 
```

### Option 2: Using Shell Script
```bash
sh ./scripts/start.sh
```

### Option 3: Docker Compose Direct
```bash
docker compose up --build
```

### Option 4: Local Development
```bash
mvn clean install
mvn spring-boot:run
```

## 📚 API Documentation

Once the application is running, access the interactive API documentation at:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Test Coverage
The project includes comprehensive test coverage for:
- Unit tests for business logic
- Integration tests for API endpoints
- Validation tests for edge cases
- Sample data scenarios in `sample-data/` directory


### Aspect-Oriented Programming (AOP)
- **Logging Aspect**: Automatic exception logging
- **Performance Monitoring**: Method execution time tracking
- **Security Aspects**: Cross-cutting security concerns


### Environment Variables
```bash
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=fx_deals
DB_USERNAME=admin
DB_PASSWORD=password

# Application Configuration
SERVER_PORT=8080
LOG_LEVEL=INFO
```

### Docker Environment
All configurations are externalized and can be overridden via environment variables or Docker Compose.

## 📝 Development Guidelines

### Code Style
- Follow Spring Boot best practices
- Use Lombok annotations to reduce boilerplate
- Implement comprehensive validation
- Write meaningful commit messages

### Error Handling
- All exceptions are logged via AOP
- Business logic remains clean and focused
- Detailed error responses for API consumers


```

