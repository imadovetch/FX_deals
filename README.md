# FX Deals Application

A Spring Boot application for importing and managing FX (Foreign Exchange) deals with comprehensive validation, duplicate detection, and robust error handling.

## 🚀 Features

- **Bulk Deal Import**: Import multiple FX deals in a single API call
- **Duplicate Detection**: Automatic filtering of duplicate deals using AOP
- **Comprehensive Validation**: Bean validation with custom business rules
- **Error Handling**: Graceful error handling with detailed response messages
- **Audit Trail**: Automatic tracking of creation and modification timestamps
- **Database Optimization**: Proper indexing and constraints
- **Docker Support**: Complete containerized deployment
- **Comprehensive Testing**: Unit tests, integration tests, and API tests

## 🏗️ Architecture

### Technology Stack

- **Backend**: Spring Boot 3.5.4
- **Database**: PostgreSQL 15
- **ORM**: Spring Data JPA with Hibernate
- **Validation**: Bean Validation (Jakarta)
- **Mapping**: MapStruct
- **AOP**: Spring AOP for cross-cutting concerns
- **Testing**: JUnit 5, Mockito, TestContainers
- **Containerization**: Docker & Docker Compose
- **Build Tool**: Maven

### Project Structure

```
FX_deals/
├── src/
│   ├── main/java/org/bloomberg/fx_deals/
│   │   ├── aspect/           # AOP for duplicate filtering
│   │   ├── controller/       # REST API endpoints
│   │   ├── Corevalidation/   # Business validation logic
│   │   ├── Exceptions/       # Global exception handling
│   │   ├── Helpers/          # Utility classes
│   │   ├── Mapper/           # DTO-Entity mapping
│   │   ├── Model/            # DTOs and Entities
│   │   ├── repository/       # Data access layer
│   │   ├── service/          # Business logic
│   │   └── context/          # Thread-local context
│   └── test/                 # Comprehensive test suite
├── docker-compose.yml        # Complete deployment stack
├── Dockerfile               # Multi-stage build
├── sample-data/             # Test data files
└── scripts/                 # Deployment and testing scripts
```

## 🚀 Quick Start

### Prerequisites

- Docker and Docker Compose
- Java 17 (for local development)
- Maven (for local development)

### Deployment

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd FX_deals
   ```

2. **Deploy with Docker Compose**
   ```bash
   # Make scripts executable
   chmod +x scripts/*.sh
   
   # Deploy the application
   ./scripts/deploy.sh
   ```

3. **Test the API**
   ```bash
   # Test with sample data
   ./scripts/test-api.sh
   
   # Or test manually
   curl -X POST http://localhost:8080/api/deals/import \
     -H "Content-Type: application/json" \
     -d @sample-data/deals-sample.json
   ```

### Local Development

1. **Setup Database**
   ```bash
   # Start PostgreSQL
   docker-compose up postgres -d
   ```

2. **Run Application**
   ```bash
   mvn spring-boot:run
   ```

3. **Run Tests**
   ```bash
   mvn test
   mvn jacoco:report  # Generate coverage report
   ```

## 📋 API Documentation

### Import Deals

**Endpoint**: `POST /api/deals/import`

**Request Body**:
```json
[
  {
    "dealUniqueId": "DEAL001",
    "fromCurrencyIsoCode": "USD",
    "toCurrencyIsoCode": "EUR",
    "dealTimestamp": 1640995200000,
    "dealAmountInOrderingCurrency": 1000000.00
  }
]
```

**Response**:
```json
{
  "message": "All deals imported successfully. | Success: 1 | Failed: 0 | Duplicates: 0",
  "successfulDeals": ["DEAL001"],
  "failedDeals": [],
  "duplicateDeals": []
}
```

### Validation Rules

- `dealUniqueId`: Required, max 255 characters
- `fromCurrencyIsoCode`: Required, exactly 3 uppercase letters
- `toCurrencyIsoCode`: Required, exactly 3 uppercase letters
- `dealTimestamp`: Required, positive number (epoch milliseconds)
- `dealAmountInOrderingCurrency`: Required, > 0, max 15 digits with 2 decimals

## 🧪 Testing

### Test Coverage

The project includes comprehensive testing with:

- **Unit Tests**: Controller, Service, Mapper, AOP, Helper classes
- **Integration Tests**: Full application stack testing
- **API Tests**: End-to-end API testing with various scenarios

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage
mvn clean test jacoco:report

# Run specific test categories
mvn test -Dtest=DealControllerTest
mvn test -Dtest=DealIntegrationTest
```

### Test Scenarios

- ✅ Valid deal import
- ✅ Bulk deal import
- ✅ Duplicate detection
- ✅ Validation errors
- ✅ Empty input handling
- ✅ Large dataset processing
- ✅ Error handling

## 🐳 Docker Deployment

### Services

- **fx-deals-app**: Spring Boot application (port 8080)
- **postgres**: PostgreSQL database (port 5432)
- **pgadmin**: Database management (port 5050, optional)

### Commands

```bash
# Deploy
./scripts/deploy.sh

# Stop
./scripts/deploy.sh stop

# Restart
./scripts/deploy.sh restart

# View logs
./scripts/deploy.sh logs

# Clean up
./scripts/deploy.sh clean
```

## 📊 Monitoring

### Health Checks

- Application: `http://localhost:8080/actuator/health`
- Database: Built-in PostgreSQL health check
- Container: Docker health checks configured

### Logging

- Application logs: `docker-compose logs fx-deals-app`
- Database logs: `docker-compose logs postgres`

## 🔧 Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | Database URL | `jdbc:postgresql://postgres:5432/fxdeals` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `fxuser` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `fxpass` |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | DDL mode | `update` |

### Database Configuration

- **Database**: PostgreSQL 15
- **Schema**: Auto-generated from JPA entities
- **Indexes**: Automatic on frequently queried columns
- **Audit**: Automatic timestamp tracking

## 🛠️ Development

### Code Quality

- **JaCoCo**: Code coverage reporting (target: 80%)
- **Checkstyle**: Code style enforcement
- **SpotBugs**: Static analysis
- **PMD**: Code quality analysis

### Best Practices

- ✅ Separation of concerns
- ✅ Dependency injection
- ✅ AOP for cross-cutting concerns
- ✅ Comprehensive error handling
- ✅ Input validation
- ✅ Database optimization
- ✅ Security considerations
- ✅ Comprehensive testing

## 📈 Performance

### Optimizations

- **Database Indexes**: On deal timestamp and currency columns
- **Batch Processing**: Efficient bulk operations
- **Connection Pooling**: HikariCP for database connections
- **Caching**: Entity-level caching where appropriate

### Monitoring

- **Health Checks**: Application and database health monitoring
- **Logging**: Structured logging with SLF4J
- **Metrics**: Spring Boot Actuator metrics

## 🔒 Security

### Implemented Measures

- **Input Validation**: Comprehensive bean validation
- **SQL Injection Prevention**: JPA/Hibernate ORM
- **Error Handling**: No sensitive information in error messages
- **Container Security**: Non-root user in Docker containers

## 📝 Sample Data

The `sample-data/deals-sample.json` file contains 10 sample FX deals with various currencies and amounts for testing purposes.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 🆘 Support

For issues and questions:

1. Check the logs: `./scripts/deploy.sh logs`
2. Run tests: `./scripts/test-api.sh`
3. Check health: `curl http://localhost:8080/actuator/health`
4. Review documentation in this README

## 🔄 Version History

- **v1.0.0**: Initial release with core functionality
  - Bulk deal import
  - Duplicate detection
  - Comprehensive validation
  - Docker deployment
  - Complete test suite 