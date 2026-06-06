# RevCast - Enterprise Grade Revenue Forecasting System

## Overview

RevCast is a comprehensive enterprise-grade revenue forecasting system built with Spring Boot 3.x and designed to automate organizational revenue forecasting using employee allocation data, leaves, resignations, backfills, and open positions.

## Features

### Core Features
- **Automated Revenue Forecasting**: Generate weekly, monthly, and quarterly forecasts
- **Resource Management**: Track employee allocations and reallocations
- **Resignation Handling**: Automatic backfill creation and STBO conversion
- **Leave Management**: Calculate billable days considering leaves and holidays
- **Variance Analysis**: Compare forecasts and identify variance reasons
- **Role-Based Access Control**: ADMIN, PROJECT_MANAGER, DELIVERY_HEAD roles
- **JWT Authentication**: Secure API endpoints with token-based authentication

### Business Logic
- **SBL (Soft Backlog) Revenue**: Revenue from actively employed and billable resources
- **STBO (Sold To Be Offered) Revenue**: Revenue from positions sold but not yet filled
- **Automatic Conversions**: STBO automatically converts to SBL when filled
- **Leave Adjustments**: Automatic billable days calculation excluding leaves and holidays
- **Resignation Rules**: Revenue forecasting stops after employee's last working day

## Technology Stack

- **Framework**: Spring Boot 3.3.0
- **Language**: Java 21
- **Database**: MySQL 8
- **ORM**: Spring Data JPA (Hibernate)
- **Security**: Spring Security + JWT
- **Build Tool**: Maven
- **Database Migrations**: Flyway
- **Documentation**: Swagger/OpenAPI 3.0
- **Testing**: JUnit 5, Mockito
- **Object Mapping**: MapStruct
- **Utilities**: Lombok, Apache POI, Apache Commons CSV

## Project Structure

```
com.revcast
├── config                          # Configuration classes
│   ├── SecurityConfig.java
│   ├── AuditingConfig.java
│   └── CorsConfig.java
├── security                        # Security components
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
├── common                          # Common utilities
│   ├── exception/                  # Custom exceptions
│   ├── response/                   # API response wrappers
│   ├── constants/                  # Application constants
│   ├── util/                       # Utility classes
│   └── audit/                      # Auditing support
├── auth                            # Authentication module
│   ├── controller/
│   ├── service/
│   └── dto/
├── user                            # User management
│   ├── entity/
│   ├── repository/
│   └── service/
├── employee                        # Employee management
│   ├── entity/
│   ├── repository/
│   ├── service/
│   ├── controller/
│   └── dto/
├── allocation                      # Resource allocation
│   ├── entity/
│   ├── repository/
│   ├── service/
│   ├── controller/
│   └── dto/
├── forecast                        # Forecast engine
│   ├── entity/
│   ├── repository/
│   ├── service/
│   ├── controller/
│   └── dto/
├── variance                        # Variance analysis
│   ├── entity/
│   ├── repository/
│   ├── service/
│   ├── controller/
│   └── dto/
├── leave                           # Leave management
│   ├── entity/
│   ├── repository/
│   └── service/
├── holiday                         # Holiday management
│   ├── entity/
│   ├── repository/
│   └── service/
├── resignation                     # Resignation tracking
│   ├── entity/
│   ├── repository/
│   └── service/
├── backfill                        # Backfill management
│   ├── entity/
│   ├── repository/
│   ├── service/
│   └── controller/
├── project                         # Project management
│   ├── entity/
│   ├── repository/
│   └── service/
├── account                         # Account & Segment
│   ├── entity/
│   ├── repository/
│   └── service/
├── reporting                       # Reporting module
│   └── controller/
├── scheduler                       # Scheduled jobs
│   └── ForecastScheduler.java
└── ingestion                       # Data ingestion
    └── controller/
```

## Setup Instructions

### Prerequisites
- Java 21 JDK
- MySQL 8.0+
- Maven 3.8+

### Installation

1. **Clone Repository**
```bash
git clone <repository-url>
cd revcast
```

2. **Configure Database**
```bash
# Create database
CREATE DATABASE revcast_db;
CREATE USER 'revcast'@'localhost' IDENTIFIED BY 'revcast_password';
GRANT ALL PRIVILEGES ON revcast_db.* TO 'revcast'@'localhost';
FLUSH PRIVILEGES;
```

3. **Update Configuration**
Edit `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/revcast_db
    username: revcast
    password: revcast_password
```

4. **Build Application**
```bash
mvn clean package
```

5. **Run Application**
```bash
java -jar target/revcast-1.0.0.jar
```

Application starts at `http://localhost:8080`

## API Documentation

### Swagger UI
Access API documentation at: `http://localhost:8080/swagger-ui.html`

### Authentication Endpoints

#### Login
```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin@123"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "user": {
      "id": 1,
      "username": "admin",
      "email": "admin@example.com",
      "roleName": "ADMIN"
    }
  }
}
```

### Employee Endpoints

#### Create Employee
```http
POST /api/v1/employees
Authorization: Bearer <token>
Content-Type: application/json

{
  "employeeCode": "EMP001",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "dateOfJoining": "2026-01-01",
  "billingRate": 100.00,
  "designation": "Software Engineer"
}
```

#### Get Employee
```http
GET /api/v1/employees/{id}
Authorization: Bearer <token>
```

#### Get All Active Employees
```http
GET /api/v1/employees
Authorization: Bearer <token>
```

#### Update Employee
```http
PUT /api/v1/employees/{id}
Authorization: Bearer <token>
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "billingRate": 120.00
}
```

### Resource Allocation Endpoints

#### Allocate Employee
```http
POST /api/v1/allocations
Authorization: Bearer <token>
Content-Type: application/json

{
  "employeeId": 1,
  "projectId": 1,
  "startDate": "2026-01-01",
  "endDate": "2026-12-31",
  "allocationPercentage": 100,
  "billingRate": 100.00,
  "hoursPerDay": 8
}
```

#### Get Allocations for Employee
```http
GET /api/v1/allocations/employee/{employeeId}
Authorization: Bearer <token>
```

#### Get Allocations for Project
```http
GET /api/v1/allocations/project/{projectId}
Authorization: Bearer <token>
```

### Forecast Endpoints

#### Generate Forecast
```http
POST /api/v1/forecasts/generate
Authorization: Bearer <token>
Content-Type: application/json

{
  "forecastType": "MONTHLY",
  "startDate": "2026-01-01",
  "endDate": "2026-01-31",
  "accountId": null,
  "segmentId": null,
  "projectId": null
}
```

#### Get Forecast
```http
GET /api/v1/forecasts/{id}
Authorization: Bearer <token>
```

#### Get Forecasts by Type
```http
GET /api/v1/forecasts/type/{forecastType}
Authorization: Bearer <token>
```

### Variance Analysis Endpoints

#### Analyze Variance
```http
POST /api/v1/variance/analyze/{forecastId}
Authorization: Bearer <token>
```

**Response:**
```json
{
  "success": true,
  "data": {
    "id": 1,
    "currentForecastId": 1,
    "previousForecastId": null,
    "varianceAmount": 50000,
    "variancePercentage": 5.2,
    "reasons": [
      {
        "reason": "Employee Joined",
        "impact": 30000,
        "description": "New employee allocation"
      },
      {
        "reason": "Leave Added",
        "impact": -10000,
        "description": "Employee leave deduction"
      }
    ]
  }
}
```

## Database Schema

### Key Tables

#### employees
- Core employee information
- Tracks joining date and last working day
- Stores billing rate

#### resource_allocations
- Employee allocation to projects
- Contains allocation percentage and billing rate
- Tracks allocation period

#### stbo_positions
- Sold To Be Offered positions
- Represents unfilled revenue
- Converts to SBL when filled

#### leave_records
- Employee leave records
- Links leaves to employees
- Affects billable days calculation

#### holidays
- Company and client holidays
- Used to calculate working days

#### resignations
- Employee resignation tracking
- Triggers backfill creation
- Stops revenue forecasting after LWD

#### forecasts
- Forecast records
- Stores SBL, STBO, and total revenue

#### forecast_details
- Detailed breakdown of forecast
- One record per allocation/STBO

#### variance_analysis
- Stores variance between forecasts
- Contains variance reasons

## Business Rules

### Revenue Calculation
```
Revenue = Billable Hours × Billing Rate

Billable Hours = Billable Days × Hours Per Day

Billable Days = Total Days - Weekends - Holidays - Leaves
```

### STBO Conversion
- When a resource is assigned to a STBO position, STBO automatically converts to SBL
- SBL revenue calculation starts from the employee's start date
- STBO revenue ends when employee joins

### Resignation Handling
- When employee resigns, revenue forecasting stops after Last Working Day
- Backfill request is automatically created
- New STBO position created for backfill continuation

### Leave Adjustments
- Approved leaves are excluded from billable days
- Partial allocation percentages reduce billable days proportionally

## Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn integration-test
```

### Test Coverage
```bash
mvn jacoco:report
# Coverage report: target/site/jacoco/index.html
```

## Scheduled Jobs

The following jobs run automatically:

| Job | Schedule | Purpose |
|-----|----------|---------|
| Weekly Forecast | Monday 2:00 AM | Generate weekly forecast |
| Monthly Forecast | 1st, 2:00 AM | Generate monthly forecast |
| Quarterly Forecast | Q-start 2:00 AM | Generate quarterly forecast |
| Daily Refresh | Daily 3:00 AM | Refresh active forecasts |

## Security

### Authentication
- JWT-based stateless authentication
- Tokens expire in 24 hours
- Refresh tokens available for 7 days

### Authorization
- Role-based access control (RBAC)
- Three roles: ADMIN, PROJECT_MANAGER, DELIVERY_HEAD
- Method-level security annotations

### Data Protection
- All passwords encrypted with BCrypt
- Sensitive data never logged
- Audit trail for all changes

## Performance Considerations

- Connection pooling with default pool size 10
- Database indexes on frequently queried columns
- Batch processing for bulk operations
- Caching for reference data (holidays, roles)

## Troubleshooting

### Database Connection Issues
```
Error: Access denied for user 'revcast'
Solution: Verify MySQL credentials in application.yml
```

### JWT Token Validation Fails
```
Error: Invalid JWT token
Solution: Check token expiration and signature configuration
```

### Forecast Generation Fails
```
Error: Forecast calculation error
Solution: Verify all allocations have valid employee and project references
```

## Future Enhancements

- Excel export functionality
- Advanced variance analysis with ML predictions
- Multi-currency support
- Team-based hierarchies
- Budget vs. Forecast comparison
- Client billing integration
- Time tracking integration

## Support & Documentation

- API Documentation: http://localhost:8080/swagger-ui.html
- Database Schema: See `src/main/resources/db/migration/V1__Initial_Schema.sql`
- Architecture Docs: See `docs/architecture.md`

## License

Proprietary - All rights reserved

## Version History

### v1.0.0 (2026-06-06)
- Initial release
- Core forecast engine
- Employee and resource management
- Variance analysis
- Role-based security

