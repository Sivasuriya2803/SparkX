# RevCast Project Structure

## Directory Layout

```
revcast/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/revcast/
│   │   │       ├── RevCastApplication.java          # Main application entry point
│   │   │       ├── config/                          # Configuration classes
│   │   │       │   ├── SecurityConfig.java          # Spring Security configuration
│   │   │       │   ├── AuditingConfig.java          # JPA auditing configuration
│   │   │       │   ├── CorsConfig.java              # CORS configuration
│   │   │       │   └── WebConfig.java               # Web configuration
│   │   │       ├── security/                        # Security components
│   │   │       │   ├── JwtTokenProvider.java        # JWT token generation/validation
│   │   │       │   ├── JwtAuthenticationFilter.java # JWT filter
│   │   │       │   └── CustomUserDetailsService.java# User details service
│   │   │       ├── common/                          # Common utilities
│   │   │       │   ├── exception/                   # Custom exceptions
│   │   │       │   │   ├── RevCastException.java
│   │   │       │   │   ├── ResourceNotFoundException.java
│   │   │       │   │   ├── ValidationException.java
│   │   │       │   │   ├── ForecastException.java
│   │   │       │   │   ├── UnauthorizedException.java
│   │   │       │   │   └── GlobalExceptionHandler.java
│   │   │       │   ├── response/                    # API response wrappers
│   │   │       │   │   ├── ApiResponse.java
│   │   │       │   │   └── ErrorDetails.java
│   │   │       │   ├── constants/                   # Application constants
│   │   │       │   │   └── AppConstants.java
│   │   │       │   ├── util/                        # Utility classes
│   │   │       │   │   └── CalendarUtil.java        # Calendar calculations
│   │   │       │   └── audit/                       # Auditing support
│   │   │       │       ├── BaseEntity.java          # Base entity with audit fields
│   │   │       │       └── AuditLog.java            # Audit logging entity
│   │   │       ├── auth/                            # Authentication module
│   │   │       │   ├── controller/
│   │   │       │   │   └── AuthenticationController.java
│   │   │       │   ├── service/
│   │   │       │   │   └── AuthenticationService.java
│   │   │       │   └── dto/
│   │   │       │       ├── LoginRequest.java
│   │   │       │       ├── LoginResponse.java
│   │   │       │       └── UserDTO.java
│   │   │       ├── user/                            # User management
│   │   │       │   ├── entity/
│   │   │       │   │   ├── Role.java
│   │   │       │   │   └── User.java
│   │   │       │   ├── repository/
│   │   │       │   │   ├── RoleRepository.java
│   │   │       │   │   └── UserRepository.java
│   │   │       │   └── service/
│   │   │       │       └── UserService.java
│   │   │       ├── employee/                        # Employee management
│   │   │       │   ├── entity/
│   │   │       │   │   └── Employee.java
│   │   │       │   ├── repository/
│   │   │       │   │   └── EmployeeRepository.java
│   │   │       │   ├── service/
│   │   │       │   │   └── EmployeeService.java
│   │   │       │   ├── controller/
│   │   │       │   │   └── EmployeeController.java
│   │   │       │   └── dto/
│   │   │       │       ├── EmployeeCreateRequest.java
│   │   │       │       └── EmployeeResponse.java
│   │   │       ├── allocation/                      # Resource allocation
│   │   │       │   ├── entity/
│   │   │       │   │   └── ResourceAllocation.java
│   │   │       │   ├── repository/
│   │   │       │   │   └── ResourceAllocationRepository.java
│   │   │       │   ├── service/
│   │   │       │   │   └── ResourceAllocationService.java
│   │   │       │   ├── controller/
│   │   │       │   │   └── ResourceAllocationController.java
│   │   │       │   └── dto/
│   │   │       │       ├── AllocationCreateRequest.java
│   │   │       │       └── AllocationResponse.java
│   │   │       ├── forecast/                        # Forecast engine
│   │   │       │   ├── entity/
│   │   │       │   │   ├── Forecast.java
│   │   │       │   │   └── ForecastDetail.java
│   │   │       │   ├── repository/
│   │   │       │   │   ├── ForecastRepository.java
│   │   │       │   │   └── ForecastDetailRepository.java
│   │   │       │   ├── service/
│   │   │       │   │   ├── ForecastCalculator.java  # Core calculation engine
│   │   │       │   │   └── ForecastService.java     # Forecast orchestration
│   │   │       │   ├── controller/
│   │   │       │   │   └── ForecastController.java
│   │   │       │   └── dto/
│   │   │       │       ├── ForecastGenerateRequest.java
│   │   │       │       ├── ForecastResponse.java
│   │   │       │       └── ForecastDetailResponse.java
│   │   │       ├── variance/                        # Variance analysis
│   │   │       │   ├── entity/
│   │   │       │   │   ├── VarianceAnalysis.java
│   │   │       │   │   └── VarianceReason.java
│   │   │       │   ├── repository/
│   │   │       │   │   └── VarianceAnalysisRepository.java
│   │   │       │   ├── service/
│   │   │       │   │   └── VarianceAnalysisService.java
│   │   │       │   ├── controller/
│   │   │       │   │   └── VarianceAnalysisController.java
│   │   │       │   └── dto/
│   │   │       │       ├── VarianceResponse.java
│   │   │       │       └── VarianceReasonResponse.java
│   │   │       ├── leave/                           # Leave management
│   │   │       │   ├── entity/
│   │   │       │   │   └── LeaveRecord.java
│   │   │       │   ├── repository/
│   │   │       │   │   └── LeaveRecordRepository.java
│   │   │       │   ├── service/
│   │   │       │   │   └── LeaveService.java
│   │   │       │   ├── controller/
│   │   │       │   │   └── LeaveController.java
│   │   │       │   └── dto/
│   │   │       │       └── LeaveCreateRequest.java
│   │   │       ├── holiday/                         # Holiday management
│   │   │       │   ├── entity/
│   │   │       │   │   └── Holiday.java
│   │   │       │   ├── repository/
│   │   │       │   │   └── HolidayRepository.java
│   │   │       │   ├── service/
│   │   │       │   │   └── HolidayService.java
│   │   │       │   ├── controller/
│   │   │       │   │   └── HolidayController.java
│   │   │       │   └── dto/
│   │   │       │       └── HolidayCreateRequest.java
│   │   │       ├── resignation/                     # Resignation tracking
│   │   │       │   ├── entity/
│   │   │       │   │   └── Resignation.java
│   │   │       │   ├── repository/
│   │   │       │   │   └── ResignationRepository.java
│   │   │       │   ├── service/
│   │   │       │   │   └── ResignationService.java
│   │   │       │   └── controller/
│   │   │       │       └── ResignationController.java
│   │   │       ├── backfill/                        # Backfill management
│   │   │       │   ├── entity/
│   │   │       │   │   ├── STBOPosition.java
│   │   │       │   │   └── BackfillRequest.java
│   │   │       │   ├── repository/
│   │   │       │   │   ├── STBOPositionRepository.java
│   │   │       │   │   └── BackfillRequestRepository.java
│   │   │       │   ├── service/
│   │   │       │   │   └── BackfillService.java
│   │   │       │   └── controller/
│   │   │       │       └── BackfillController.java
│   │   │       ├── project/                         # Project management
│   │   │       │   ├── entity/
│   │   │       │   │   └── Project.java
│   │   │       │   ├── repository/
│   │   │       │   │   └── ProjectRepository.java
│   │   │       │   └── service/
│   │   │       │       └── ProjectService.java
│   │   │       ├── account/                         # Account & Segment
│   │   │       │   ├── entity/
│   │   │       │   │   ├── Account.java
│   │   │       │   │   └── Segment.java
│   │   │       │   ├── repository/
│   │   │       │   │   ├── AccountRepository.java
│   │   │       │   │   └── SegmentRepository.java
│   │   │       │   └── service/
│   │   │       │       ├── AccountService.java
│   │   │       │       └── SegmentService.java
│   │   │       ├── reporting/                       # Reporting module
│   │   │       │   ├── controller/
│   │   │       │   │   └── ReportingController.java
│   │   │       │   └── service/
│   │   │       │       └── ReportingService.java
│   │   │       ├── scheduler/                       # Scheduled jobs
│   │   │       │   └── ForecastScheduler.java
│   │   │       └── ingestion/                       # Data ingestion
│   │   │           ├── controller/
│   │   │           │   └── DataIngestionController.java
│   │   │           ├── service/
│   │   │           │   ├── ExcelParser.java
│   │   │           │   ├── CsvParser.java
│   │   │           │   └── DataValidationService.java
│   │   │           └── dto/
│   │   │               └── ImportRequest.java
│   │   └── resources/
│   │       ├── application.yml                      # Spring configuration
│   │       ├── db/
│   │       │   └── migration/
│   │       │       ├── V1__Initial_Schema.sql       # Database schema
│   │       │       └── V2__Insert_Sample_Data.sql   # Sample data (optional)
│   │       └── logback-spring.xml                   # Logging configuration
│   └── test/
│       └── java/
│           └── com/revcast/
│               ├── unit/                            # Unit tests
│               │   ├── employee/
│               │   │   └── EmployeeServiceTest.java
│               │   ├── forecast/
│               │   │   └── ForecastCalculatorTest.java
│               │   └── allocation/
│               │       └── AllocationServiceTest.java
│               └── integration/                     # Integration tests
│                   └── ForecastIntegrationTest.java
├── pom.xml                                          # Maven configuration
├── README.md                                        # Project documentation
├── QUICKSTART.md                                    # Quick start guide
├── PROJECT_STRUCTURE.md                             # This file
└── .gitignore                                       # Git ignore rules
```

## Module Descriptions

### Config Module
Centralized configuration for:
- Spring Security (JWT, CORS)
- JPA Auditing
- OpenAPI/Swagger

### Security Module
Handles:
- JWT token generation and validation
- Authentication filter
- User details service
- Authority mapping

### Common Module
Shared utilities:
- Custom exceptions
- API response wrappers
- Application constants
- Calendar utilities for billable days
- Base entity with audit support

### Auth Module
Authentication flow:
- Login
- Token generation
- User information
- Logout

### User Module
User management:
- User creation
- Role assignment
- User deactivation

### Employee Module
Employee lifecycle:
- Employee creation
- Employee updates
- Deactivation
- Resignation tracking

### Allocation Module
Resource allocation:
- Allocate employees to projects
- Update allocations
- End allocations
- Query active allocations

### Forecast Module
Revenue forecasting:
- **ForecastCalculator**: Core calculation engine (Strategy pattern)
  - SBL revenue calculation
  - STBO revenue calculation
  - Leave adjustments
  - Holiday handling
- **ForecastService**: Orchestration
  - Forecast generation
  - Detail aggregation
  - Historical retrieval

### Variance Module
Variance analysis:
- Compare forecasts
- Identify variance reasons
- Generate variance reports

### Leave Module
Leave management:
- Create leave records
- Approve/reject leaves
- Query leaves by employee
- Used in forecast calculations

### Holiday Module
Holiday management:
- Company-wide holidays
- Project-specific holidays
- Used to calculate working days

### Resignation Module
Resignation tracking:
- Record resignation
- Trigger backfill
- Stops revenue forecasting

### Backfill Module
Backfill & STBO management:
- STBO position creation
- Backfill requests
- Track replacements

### Project Module
Project management:
- Create projects
- Project hierarchy (Segment → Project)
- Project allocation tracking

### Account Module
Account and segment management:
- Account creation
- Segment creation
- Hierarchy structure

### Reporting Module
Revenue reporting:
- Weekly reports
- Monthly reports
- Quarterly reports
- Revenue breakdowns

### Scheduler Module
Scheduled tasks:
- Weekly forecast generation
- Monthly forecast generation
- Quarterly forecast generation
- Daily refresh

### Ingestion Module
Data import:
- Excel file parsing
- CSV file parsing
- Data validation
- Bulk imports

## Design Patterns Used

1. **Repository Pattern**: Data access abstraction
2. **Service Pattern**: Business logic encapsulation
3. **DTO Pattern**: Data transfer between layers
4. **Strategy Pattern**: Forecast calculation strategies
5. **Builder Pattern**: Entity and DTO construction
6. **Singleton Pattern**: Spring beans
7. **Factory Pattern**: Exception factory methods

## Data Flow

```
Controller
    ↓ (DTO)
Service
    ↓ (Entity)
Repository
    ↓ (SQL)
Database
```

## Database Schema
- 15+ core tables with proper relationships
- Foreign keys and constraints
- Indexes on commonly searched columns
- Audit trail for compliance

## Testing Strategy

- **Unit Tests**: Service and calculator logic
- **Integration Tests**: End-to-end workflows
- **Repository Tests**: Data access layer
- **Controller Tests**: API endpoints

## Security Model

- JWT-based stateless authentication
- Role-based access control (RBAC)
- Method-level authorization
- Comprehensive audit logging

