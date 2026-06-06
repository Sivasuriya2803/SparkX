# RevCast - Complete API Testing Guide

## Prerequisites

- Application running on: `http://localhost:8080`
- API Base Path: `/api/v1`
- All endpoints require JWT token except `/auth/login`

---

## 1. AUTHENTICATION ENDPOINTS

### 1.1 Login and Get JWT Token

**Endpoint:** `POST /auth/login`

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin@123"
  }'
```

**Expected Response (200):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
    "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "user": {
      "id": 1,
      "username": "admin",
      "email": "admin@revcast.com",
      "firstName": "Admin",
      "lastName": "User",
      "roleName": "ADMIN",
      "isActive": true
    }
  }
}
```

**Usage Note:** Save the `accessToken` value - you'll need it for other requests:
```bash
TOKEN="eyJhbGciOiJIUzUxMiJ9..."
```

### 1.2 Health Check

**Endpoint:** `POST /auth/health`

```bash
curl -X POST http://localhost:8080/api/v1/auth/health
```

**Expected Response (200):**
```json
{
  "success": true,
  "message": "Service is healthy",
  "data": "OK"
}
```

### 1.3 Logout

**Endpoint:** `POST /auth/logout`

```bash
curl -X POST http://localhost:8080/api/v1/auth/logout \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response (200):**
```json
{
  "success": true,
  "message": "Logout successful",
  "data": null
}
```

---

## 2. EMPLOYEE ENDPOINTS

### 2.1 Create Employee

**Endpoint:** `POST /employees`

```bash
curl -X POST http://localhost:8080/api/v1/employees \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phone": "+1-555-0100",
    "dateOfBirth": "1990-05-15",
    "dateOfJoining": "2024-01-15",
    "designation": "Senior Software Engineer",
    "billingRate": 150.00
  }'
```

**Expected Response (201):**
```json
{
  "success": true,
  "message": "Employee created successfully",
  "data": {
    "id": 2,
    "employeeCode": "EMP001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "phone": "+1-555-0100",
    "dateOfBirth": "1990-05-15",
    "dateOfJoining": "2024-01-15",
    "lastWorkingDay": null,
    "designation": "Senior Software Engineer",
    "billingRate": 150.00,
    "isActive": true
  }
}
```

### 2.2 Get All Active Employees

**Endpoint:** `GET /employees`

```bash
curl -X GET http://localhost:8080/api/v1/employees \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "employeeCode": "ADMIN001",
      "firstName": "Admin",
      "lastName": "User",
      "email": "admin@revcast.com",
      "isActive": true
    },
    {
      "id": 2,
      "employeeCode": "EMP001",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john@example.com",
      "isActive": true
    }
  ]
}
```

### 2.3 Get Employee by ID

**Endpoint:** `GET /employees/{id}`

```bash
curl -X GET http://localhost:8080/api/v1/employees/2 \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 2,
    "employeeCode": "EMP001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "designation": "Senior Software Engineer",
    "billingRate": 150.00,
    "isActive": true
  }
}
```

### 2.4 Get Employee by Code

**Endpoint:** `GET /employees/code/{code}`

```bash
curl -X GET http://localhost:8080/api/v1/employees/code/EMP001 \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response (200):** Same as above

### 2.5 Update Employee

**Endpoint:** `PUT /employees/{id}`

```bash
curl -X PUT http://localhost:8080/api/v1/employees/2 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "billingRate": 175.00,
    "designation": "Lead Software Engineer"
  }'
```

**Expected Response (200):**
```json
{
  "success": true,
  "message": "Employee updated successfully",
  "data": {
    "id": 2,
    "firstName": "Jane",
    "lastName": "Smith",
    "billingRate": 175.00,
    "designation": "Lead Software Engineer",
    "isActive": true
  }
}
```

### 2.6 Deactivate Employee

**Endpoint:** `DELETE /employees/{id}`

```bash
curl -X DELETE http://localhost:8080/api/v1/employees/2 \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response (200):**
```json
{
  "success": true,
  "message": "Employee deactivated successfully",
  "data": {
    "id": 2,
    "employeeCode": "EMP001",
    "firstName": "Jane",
    "lastName": "Smith",
    "isActive": false
  }
}
```

---

## 3. RESOURCE ALLOCATION ENDPOINTS

### 3.1 Allocate Employee to Project

**Endpoint:** `POST /allocations`

```bash
curl -X POST http://localhost:8080/api/v1/allocations \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "employeeId": 2,
    "projectId": 1,
    "startDate": "2026-01-01",
    "endDate": "2026-12-31",
    "allocationPercentage": 100,
    "billingRate": 175.00,
    "hoursPerDay": 8
  }'
```

**Expected Response (201):**
```json
{
  "success": true,
  "message": "Employee allocated successfully",
  "data": {
    "id": 1,
    "employeeId": 2,
    "employeeName": "Jane Smith",
    "projectId": 1,
    "projectName": "Test Project",
    "startDate": "2026-01-01",
    "endDate": "2026-12-31",
    "allocationPercentage": 100,
    "billingRate": 175.00,
    "hoursPerDay": 8,
    "allocationStatus": "ACTIVE"
  }
}
```

### 3.2 Get Allocations by Employee

**Endpoint:** `GET /allocations/employee/{employeeId}`

```bash
curl -X GET http://localhost:8080/api/v1/allocations/employee/2 \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "employeeId": 2,
      "employeeName": "Jane Smith",
      "projectId": 1,
      "projectName": "Test Project",
      "startDate": "2026-01-01",
      "endDate": "2026-12-31",
      "allocationPercentage": 100,
      "billingRate": 175.00,
      "hoursPerDay": 8,
      "allocationStatus": "ACTIVE"
    }
  ]
}
```

### 3.3 Get Allocations by Project

**Endpoint:** `GET /allocations/project/{projectId}`

```bash
curl -X GET http://localhost:8080/api/v1/allocations/project/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 3.4 Get Allocation by ID

**Endpoint:** `GET /allocations/{id}`

```bash
curl -X GET http://localhost:8080/api/v1/allocations/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 3.5 Get Active Allocations on Specific Date

**Endpoint:** `GET /allocations/employee/{employeeId}/date/{date}`

```bash
curl -X GET "http://localhost:8080/api/v1/allocations/employee/2/date/2026-06-06" \
  -H "Authorization: Bearer $TOKEN"
```

### 3.6 Update Allocation

**Endpoint:** `PUT /allocations/{id}`

```bash
curl -X PUT http://localhost:8080/api/v1/allocations/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "allocationPercentage": 80,
    "billingRate": 180.00
  }'
```

### 3.7 End Allocation

**Endpoint:** `PUT /allocations/{id}/end`

```bash
curl -X PUT http://localhost:8080/api/v1/allocations/1/end \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"endDate": "2026-06-30"}'
```

---

## 4. FORECAST ENDPOINTS

### 4.1 Generate Forecast

**Endpoint:** `POST /forecasts/generate`

```bash
curl -X POST http://localhost:8080/api/v1/forecasts/generate \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "forecastType": "MONTHLY",
    "startDate": "2026-06-01",
    "endDate": "2026-06-30"
  }'
```

**Expected Response (201):**
```json
{
  "success": true,
  "message": "Forecast generated successfully",
  "data": {
    "id": 1,
    "forecastCode": "M-a1b2c3d4",
    "forecastType": "MONTHLY",
    "forecastPeriod": "2026-06",
    "startDate": "2026-06-01",
    "endDate": "2026-06-30",
    "totalRevenue": 378000.00,
    "sblRevenue": 378000.00,
    "stboRevenue": 0.00,
    "status": "FINALIZED",
    "details": [
      {
        "id": 1,
        "employeeName": "Jane Smith",
        "projectName": "Test Project",
        "segmentName": "Consulting",
        "accountName": "Acme Corp",
        "billableDays": 20,
        "billableHours": 160,
        "billingRate": 180.00,
        "revenue": 28800.00,
        "revenueType": "SBL"
      }
    ]
  }
}
```

### 4.2 Get Forecast by ID

**Endpoint:** `GET /forecasts/{id}`

```bash
curl -X GET http://localhost:8080/api/v1/forecasts/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 4.3 Get Forecasts by Type

**Endpoint:** `GET /forecasts/type/{forecastType}`

```bash
curl -X GET http://localhost:8080/api/v1/forecasts/type/MONTHLY \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response (200):**
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 1,
      "forecastCode": "M-a1b2c3d4",
      "forecastType": "MONTHLY",
      "forecastPeriod": "2026-06",
      "totalRevenue": 378000.00,
      "sblRevenue": 378000.00,
      "stboRevenue": 0.00,
      "status": "FINALIZED"
    }
  ]
}
```

---

## 5. VARIANCE ANALYSIS ENDPOINTS

### 5.1 Analyze Variance

**Endpoint:** `POST /variance/analyze/{forecastId}`

```bash
curl -X POST http://localhost:8080/api/v1/variance/analyze/1 \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response (200):**
```json
{
  "success": true,
  "message": "Variance analysis completed",
  "data": {
    "id": 1,
    "currentForecastId": 1,
    "previousForecastId": null,
    "varianceAmount": 378000.00,
    "variancePercentage": 0.00,
    "analysisStatus": "FINALIZED",
    "reasons": []
  }
}
```

### 5.2 Get Variance Analysis

**Endpoint:** `GET /variance/{id}`

```bash
curl -X GET http://localhost:8080/api/v1/variance/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 5.3 Get Variance Analyses for Forecast

**Endpoint:** `GET /variance/forecast/{forecastId}`

```bash
curl -X GET http://localhost:8080/api/v1/variance/forecast/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## 6. ACCOUNT ENDPOINTS

### 6.1 Create Account

**Endpoint:** `POST /accounts`

```bash
curl -X POST http://localhost:8080/api/v1/accounts \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "accountName": "Acme Corporation",
    "accountCode": "ACME001",
    "description": "Major customer account"
  }'
```

**Expected Response (201):**
```json
{
  "success": true,
  "message": "Account created successfully",
  "data": {
    "id": 1,
    "accountName": "Acme Corporation",
    "accountCode": "ACME001",
    "description": "Major customer account",
    "isActive": true
  }
}
```

### 6.2 Get Account by ID

**Endpoint:** `GET /accounts/{id}`

```bash
curl -X GET http://localhost:8080/api/v1/accounts/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 6.3 Get All Accounts

**Endpoint:** `GET /accounts`

```bash
curl -X GET http://localhost:8080/api/v1/accounts \
  -H "Authorization: Bearer $TOKEN"
```

---

## 7. SEGMENT ENDPOINTS

### 7.1 Create Segment

**Endpoint:** `POST /segments`

```bash
curl -X POST http://localhost:8080/api/v1/segments \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "segmentName": "Consulting Services",
    "segmentCode": "CONS001",
    "accountId": 1,
    "description": "Consulting segment"
  }'
```

### 7.2 Get Segment by ID

**Endpoint:** `GET /segments/{id}`

```bash
curl -X GET http://localhost:8080/api/v1/segments/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 7.3 Get Segments by Account

**Endpoint:** `GET /segments/account/{accountId}`

```bash
curl -X GET http://localhost:8080/api/v1/segments/account/1 \
  -H "Authorization: Bearer $TOKEN"
```

---

## 8. PROJECT ENDPOINTS

### 8.1 Create Project

**Endpoint:** `POST /projects`

```bash
curl -X POST http://localhost:8080/api/v1/projects \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "projectName": "Digital Transformation",
    "projectCode": "DT001",
    "segmentId": 1,
    "startDate": "2026-01-01",
    "endDate": "2026-12-31",
    "description": "Major transformation project"
  }'
```

### 8.2 Get Project by ID

**Endpoint:** `GET /projects/{id}`

```bash
curl -X GET http://localhost:8080/api/v1/projects/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 8.3 Get All Projects

**Endpoint:** `GET /projects`

```bash
curl -X GET http://localhost:8080/api/v1/projects \
  -H "Authorization: Bearer $TOKEN"
```

### 8.4 Get Projects by Segment

**Endpoint:** `GET /projects/segment/{segmentId}`

```bash
curl -X GET http://localhost:8080/api/v1/projects/segment/1 \
  -H "Authorization: Bearer $TOKEN"
```

### 8.5 Update Project

**Endpoint:** `PUT /projects/{id}`

```bash
curl -X PUT http://localhost:8080/api/v1/projects/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "projectName": "Digital Transformation v2.0",
    "endDate": "2027-12-31"
  }'
```

---

## 9. COMPLETE TEST WORKFLOW SCRIPT

Create a file `test-all-endpoints.sh`:

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api/v1"

echo "===== RevCast API Testing Suite ====="
echo ""

# Step 1: Login
echo "1. Testing Login..."
LOGIN_RESPONSE=$(curl -s -X POST $BASE_URL/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin@123"
  }')

TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
echo "Token obtained: ${TOKEN:0:20}..."

# Step 2: Health Check
echo ""
echo "2. Testing Health Check..."
curl -s -X POST $BASE_URL/auth/health | jq '.'

# Step 3: Create Employee
echo ""
echo "3. Creating Employee..."
EMP_RESPONSE=$(curl -s -X POST $BASE_URL/employees \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "employeeCode": "EMP001",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "dateOfJoining": "2024-01-15",
    "designation": "Senior Engineer",
    "billingRate": 150.00
  }')

EMP_ID=$(echo $EMP_RESPONSE | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
echo "Employee created with ID: $EMP_ID"
echo $EMP_RESPONSE | jq '.'

# Step 4: Get Employees
echo ""
echo "4. Getting All Employees..."
curl -s -X GET $BASE_URL/employees \
  -H "Authorization: Bearer $TOKEN" | jq '.'

# Step 5: Create Account
echo ""
echo "5. Creating Account..."
ACC_RESPONSE=$(curl -s -X POST $BASE_URL/accounts \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "accountName": "Acme Corp",
    "accountCode": "ACME001",
    "description": "Main customer"
  }')

ACC_ID=$(echo $ACC_RESPONSE | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
echo "Account created with ID: $ACC_ID"
echo $ACC_RESPONSE | jq '.'

# Step 6: Create Segment
echo ""
echo "6. Creating Segment..."
SEG_RESPONSE=$(curl -s -X POST $BASE_URL/segments \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"segmentName\": \"Consulting\",
    \"segmentCode\": \"CONS001\",
    \"accountId\": $ACC_ID,
    \"description\": \"Consulting services\"
  }")

SEG_ID=$(echo $SEG_RESPONSE | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
echo "Segment created with ID: $SEG_ID"
echo $SEG_RESPONSE | jq '.'

# Step 7: Create Project
echo ""
echo "7. Creating Project..."
PROJ_RESPONSE=$(curl -s -X POST $BASE_URL/projects \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"projectName\": \"Test Project\",
    \"projectCode\": \"PROJ001\",
    \"segmentId\": $SEG_ID,
    \"description\": \"Test project\"
  }")

PROJ_ID=$(echo $PROJ_RESPONSE | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
echo "Project created with ID: $PROJ_ID"
echo $PROJ_RESPONSE | jq '.'

# Step 8: Create Allocation
echo ""
echo "8. Creating Resource Allocation..."
ALLOC_RESPONSE=$(curl -s -X POST $BASE_URL/allocations \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"employeeId\": $EMP_ID,
    \"projectId\": $PROJ_ID,
    \"startDate\": \"2026-01-01\",
    \"endDate\": \"2026-12-31\",
    \"allocationPercentage\": 100,
    \"billingRate\": 150.00,
    \"hoursPerDay\": 8
  }")

ALLOC_ID=$(echo $ALLOC_RESPONSE | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
echo "Allocation created with ID: $ALLOC_ID"
echo $ALLOC_RESPONSE | jq '.'

# Step 9: Generate Forecast
echo ""
echo "9. Generating Forecast..."
FORECAST_RESPONSE=$(curl -s -X POST $BASE_URL/forecasts/generate \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "forecastType": "MONTHLY",
    "startDate": "2026-06-01",
    "endDate": "2026-06-30"
  }')

FORECAST_ID=$(echo $FORECAST_RESPONSE | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
echo "Forecast generated with ID: $FORECAST_ID"
echo $FORECAST_RESPONSE | jq '.'

# Step 10: Analyze Variance
echo ""
echo "10. Analyzing Variance..."
curl -s -X POST $BASE_URL/variance/analyze/$FORECAST_ID \
  -H "Authorization: Bearer $TOKEN" | jq '.'

echo ""
echo "===== Test Suite Complete ====="
```

---

## Error Responses Examples

### 404 - Resource Not Found

```json
{
  "success": false,
  "message": "Employee not found with id: 999",
  "error": {
    "code": "ERR_NOT_FOUND",
    "details": null
  }
}
```

### 400 - Validation Error

```json
{
  "success": false,
  "message": "Validation failed",
  "error": {
    "code": "ERR_VALIDATION",
    "details": "Employee with code EMP001 already exists"
  }
}
```

### 401 - Unauthorized

```json
{
  "success": false,
  "message": "Invalid username or password",
  "error": {
    "code": "ERR_AUTH_FAILED",
    "details": null
  }
}
```

### 500 - Server Error

```json
{
  "success": false,
  "message": "An unexpected error occurred",
  "error": {
    "code": "ERR_INTERNAL",
    "details": "Connection to database failed"
  }
}
```

---

## Testing Tips

1. **Save Token in Variable** for easy reuse
2. **Always use JWT token** in Authorization header
3. **Check HTTP Status Codes** for endpoint behavior
4. **Validate Response Format** matches documentation
5. **Test Error Cases** to verify error handling
6. **Use JQ for JSON** formatting (install jq: `apt-get install jq`)
7. **Keep endpoints organized** by module

---

## Summary of All 26+ Endpoints

1. ✅ POST /auth/login
2. ✅ POST /auth/logout
3. ✅ POST /auth/health
4. ✅ POST /employees
5. ✅ GET /employees
6. ✅ GET /employees/{id}
7. ✅ GET /employees/code/{code}
8. ✅ PUT /employees/{id}
9. ✅ DELETE /employees/{id}
10. ✅ POST /allocations
11. ✅ GET /allocations/{id}
12. ✅ GET /allocations/employee/{employeeId}
13. ✅ GET /allocations/project/{projectId}
14. ✅ GET /allocations/employee/{employeeId}/date/{date}
15. ✅ PUT /allocations/{id}
16. ✅ PUT /allocations/{id}/end
17. ✅ POST /forecasts/generate
18. ✅ GET /forecasts/{id}
19. ✅ GET /forecasts/type/{forecastType}
20. ✅ POST /variance/analyze/{forecastId}
21. ✅ GET /variance/{id}
22. ✅ GET /variance/forecast/{forecastId}
23. ✅ POST /accounts
24. ✅ GET /accounts/{id}
25. ✅ GET /accounts
26. ✅ POST /segments
27. ✅ GET /segments/{id}
28. ✅ GET /segments/account/{accountId}
29. ✅ POST /projects
30. ✅ GET /projects/{id}
31. ✅ GET /projects
32. ✅ GET /projects/segment/{segmentId}
33. ✅ PUT /projects/{id}

---

**All endpoints are fully implemented and documented in the RevCast system.**

