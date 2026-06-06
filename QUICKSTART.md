# RevCast Quick Start Guide

## 5-Minute Setup

### Step 1: Prerequisites
```bash
# Check Java version (must be 21+)
java -version

# Check Maven version (must be 3.8+)
mvn -version
```

### Step 2: Database Setup
```bash
# Create database and user
mysql -u root -p << EOF
CREATE DATABASE revcast_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'revcast'@'localhost' IDENTIFIED BY 'revcast@123';
GRANT ALL PRIVILEGES ON revcast_db.* TO 'revcast'@'localhost';
FLUSH PRIVILEGES;
EOF
```

### Step 3: Build & Run
```bash
# Clone and navigate
cd revcast

# Build
mvn clean package

# Run
java -jar target/revcast-1.0.0.jar
```

### Step 4: Verify
Open browser: http://localhost:8080/swagger-ui.html

## First Steps

### 1. Login (Get JWT Token)
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin@123"
  }'
```

Save the `accessToken` from response.

### 2. Create Employee
```bash
curl -X POST http://localhost:8080/api/v1/employees \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@revcast.com",
    "dateOfJoining": "2026-01-01",
    "designation": "Senior Developer",
    "billingRate": 150.00
  }'
```

Save the `id` from response (e.g., `id: 2`).

### 3. Create Project
```bash
# First, create an account (if not exists)
# Then create a segment
# Then create a project

# For now, assume projectId = 1 (created via admin panel)
```

### 4. Allocate Employee to Project
```bash
curl -X POST http://localhost:8080/api/v1/allocations \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "employeeId": 2,
    "projectId": 1,
    "startDate": "2026-01-01",
    "endDate": "2026-12-31",
    "allocationPercentage": 100,
    "billingRate": 150.00,
    "hoursPerDay": 8
  }'
```

### 5. Generate Forecast
```bash
curl -X POST http://localhost:8080/api/v1/forecasts/generate \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "forecastType": "MONTHLY",
    "startDate": "2026-01-01",
    "endDate": "2026-01-31"
  }'
```

### 6. Get Forecast Details
```bash
curl -X GET http://localhost:8080/api/v1/forecasts/1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 7. Analyze Variance
```bash
curl -X POST http://localhost:8080/api/v1/variance/analyze/1 \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

## Core Concepts

### SBL (Soft Backlog)
Revenue from actively employed and working resources:
```
SBL Revenue = Billable Days × Billing Rate × Allocation %
```

### STBO (Sold To Be Offered)
Revenue from open positions:
```
STBO Revenue = Billable Days × Billing Rate
(No allocation %, full rate per day)
```

### Billable Days
Working days excluding:
- Weekends
- Company holidays
- Employee leaves

## Common Workflows

### Allocate a New Employee

1. Create employee record
2. Create resource allocation
3. Forecast regenerates automatically
4. Monitor forecast in reports

### Handle Employee Resignation

1. Record resignation in system
2. System automatically:
   - Stops revenue after Last Working Day
   - Creates backfill request
   - Generates STBO position
3. Assign replacement
4. STBO automatically converts to SBL

### Update Billing Rate

1. Edit employee billing rate
2. Changes apply to future forecast periods
3. Previous forecasts remain unchanged
4. Variance analysis shows rate changes

### Add Company Holiday

1. Create holiday record
2. Next forecast regeneration excludes holiday
3. Billable days automatically adjust

## Troubleshooting

### Connection Refused
```
Error: Could not connect to database
Solution: Verify MySQL is running and credentials are correct
```

### JWT Token Invalid
```
Error: Invalid JWT token
Solution: 
- Get new token via /auth/login
- Check token is in Authorization header
- Format: "Bearer <token>"
```

### Forecast Shows Zero Revenue
```
Error: Forecast generated but all revenue = 0
Solutions:
1. Verify employees are allocated to projects
2. Check start date is not in future
3. Verify billing rate is set
4. Check allocation percentage > 0
```

## Default Credentials

**Note**: Change these in production!

| User | Password | Role |
|------|----------|------|
| admin | admin@123 | ADMIN |

## Key Files

| File | Purpose |
|------|---------|
| pom.xml | Maven dependencies |
| application.yml | Configuration |
| V1__Initial_Schema.sql | Database schema |
| ForecastCalculator.java | Revenue calculation logic |
| RevCastApplication.java | Main application class |

## Performance Tips

1. **Batch Operations**: Use bulk endpoints for multiple records
2. **Date Range**: Keep forecast date ranges reasonable (monthly is optimal)
3. **Indexes**: Database has indexes on commonly filtered fields
4. **Caching**: Reference data (roles, holidays) is cached

## Next Steps

1. Read full [README.md](./README.md)
2. Explore API documentation: http://localhost:8080/swagger-ui.html
3. Review [Database Schema](./src/main/resources/db/migration/V1__Initial_Schema.sql)
4. Check [Test Examples](./src/test/java/com/revcast/)

## Support

For questions or issues:
1. Check the full README.md
2. Review test cases for examples
3. Check Swagger documentation
4. Review database schema comments

---

**Happy Forecasting! 📊**

