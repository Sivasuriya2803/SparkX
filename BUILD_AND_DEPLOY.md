# RevCast - Build and Deployment Guide

## Prerequisites

- Java 21 JDK
- Maven 3.8+
- MySQL 8.0+
- Git (optional, for version control)

## Development Setup

### Step 1: Database Setup

```bash
# Login to MySQL
mysql -u root -p

# Create database
CREATE DATABASE revcast_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# Create user
CREATE USER 'revcast'@'localhost' IDENTIFIED BY 'revcast@123';

# Grant privileges
GRANT ALL PRIVILEGES ON revcast_db.* TO 'revcast'@'localhost';
FLUSH PRIVILEGES;

# Exit
EXIT;
```

### Step 2: Configuration

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/revcast_db?useSSL=false&serverTimezone=UTC
    username: revcast
    password: revcast@123
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    baselineOnMigrate: true

app:
  jwt:
    secret: your_secret_key_min_32_characters_long_here
    expiration: 86400000
```

### Step 3: Build

```bash
# Show Maven version
mvn -version

# Clean and build
mvn clean package

# Or just compile (without running tests)
mvn clean compile
```

## Running the Application

### Option 1: From Built JAR

```bash
java -jar target/revcast-1.0.0.jar
```

### Option 2: Using Maven

```bash
mvn spring-boot:run
```

### Option 3: From IDE

Right-click `RevCastApplication.java` → Run

## Verify Installation

### 1. Health Check

```bash
curl http://localhost:8080/api/v1/auth/health
```

Expected response:
```json
{
  "success": true,
  "message": "Service is healthy",
  "data": "OK"
}
```

### 2. Swagger UI

Open browser: http://localhost:8080/swagger-ui.html

### 3. Login

```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin@123"}'
```

## Building for Production

### Step 1: Update Configuration

Create `application-prod.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://prod-db-host:3306/revcast_prod
    username: revcast_prod_user
    password: ${DB_PASSWORD}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
  jpa:
    hibernate:
      ddl-auto: validate
    properties:
      hibernate:
        jdbc:
          batch_size: 50

app:
  jwt:
    secret: ${JWT_SECRET}
    expiration: 86400000
  cors:
    allowed-origins: https://yourdomain.com
```

### Step 2: Build Production JAR

```bash
mvn clean package -DskipTests -Dspring.profiles.active=prod
```

### Step 3: Create Docker Image (Optional)

Create `Dockerfile`:

```dockerfile
FROM openjdk:21-slim
COPY target/revcast-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
```

Build image:

```bash
docker build -t revcast:1.0.0 .
```

Run container:

```bash
docker run -d \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/revcast_db \
  -e SPRING_DATASOURCE_USERNAME=revcast \
  -e SPRING_DATASOURCE_PASSWORD=password \
  -e APP_JWT_SECRET=your_secret_key \
  -p 8080:8080 \
  --name revcast \
  revcast:1.0.0
```

## Running Tests

### Unit Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=EmployeeServiceTest

# Run specific test method
mvn test -Dtest=EmployeeServiceTest#testCreateEmployee_Success
```

### Integration Tests

```bash
mvn integration-test
```

### Code Coverage

```bash
# Generate coverage report
mvn clean test jacoco:report

# View report
open target/site/jacoco/index.html
```

## Troubleshooting

### Issue: Database Connection Failed

```
Error: java.sql.SQLException: Access denied for user 'revcast'@'localhost'
```

**Solution:**
```bash
# Verify MySQL is running
mysql -u root -p -e "SELECT 1;"

# Check user privileges
mysql -u root -p -e "SHOW GRANTS FOR 'revcast'@'localhost';"

# Recreate user if needed
mysql -u root -p << EOF
DROP USER IF EXISTS 'revcast'@'localhost';
CREATE USER 'revcast'@'localhost' IDENTIFIED BY 'revcast@123';
GRANT ALL PRIVILEGES ON revcast_db.* TO 'revcast'@'localhost';
FLUSH PRIVILEGES;
EOF
```

### Issue: Port 8080 Already in Use

```
Error: Address already in use: bind
```

**Solution:**
```bash
# Option 1: Use different port
java -jar target/revcast-1.0.0.jar --server.port=8081

# Option 2: Kill existing process
lsof -i :8080
kill -9 <PID>
```

### Issue: Migrations Failed

```
Error: Flyway migration failed
```

**Solution:**
```bash
# Check Flyway status
mysql -u revcast -p -D revcast_db -e "SELECT * FROM flyway_schema_history;"

# Reset Flyway (development only)
mysql -u revcast -p -D revcast_db -e "TRUNCATE TABLE flyway_schema_history;"
```

### Issue: JWT Token Expired

```
Error: Invalid JWT token
```

**Solution:**
- Get new token via `/auth/login`
- Ensure header format: `Authorization: Bearer <token>`

## Environment Variables

For production, use environment variables instead of hardcoding:

```bash
export SPRING_DATASOURCE_URL=jdbc:mysql://prod-db:3306/revcast_db
export SPRING_DATASOURCE_USERNAME=revcast_user
export SPRING_DATASOURCE_PASSWORD=secure_password
export APP_JWT_SECRET=min_32_character_secure_secret_key
export SERVER_PORT=8080
export SPRING_PROFILES_ACTIVE=prod

java -jar target/revcast-1.0.0.jar
```

## Performance Tuning

### Database Connection Pool
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 20000
```

### JVM Tuning
```bash
java -Xmx1g -Xms512m -XX:+UseG1GC \
  -jar target/revcast-1.0.0.jar
```

### Caching
```yaml
spring:
  cache:
    type: caffeine
    caffeine:
      spec: maximumSize=1000,expireAfterWrite=10m
```

## Monitoring

### Enable Actuator Endpoints

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
```

### Access Metrics

```bash
curl http://localhost:8080/actuator/metrics
curl http://localhost:8080/actuator/health
```

## Cleanup and Maintenance

### Reset Database (Development)

```bash
mysql -u revcast -p -D revcast_db -e "
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE audit_logs;
TRUNCATE TABLE variance_reasons;
TRUNCATE TABLE variance_analysis;
TRUNCATE TABLE forecast_details;
TRUNCATE TABLE forecasts;
TRUNCATE TABLE backfill_requests;
TRUNCATE TABLE resignations;
TRUNCATE TABLE holidays;
TRUNCATE TABLE leave_records;
TRUNCATE TABLE stbo_positions;
TRUNCATE TABLE resource_allocations;
TRUNCATE TABLE employees;
TRUNCATE TABLE projects;
TRUNCATE TABLE segments;
TRUNCATE TABLE accounts;
SET FOREIGN_KEY_CHECKS = 1;
"
```

### Backup Database

```bash
mysqldump -u revcast -p revcast_db > revcast_backup_$(date +%Y%m%d_%H%M%S).sql
```

### Restore Database

```bash
mysql -u revcast -p revcast_db < revcast_backup_20260606_120000.sql
```

## Deployment Checklist

- [ ] Database created and configured
- [ ] All environment variables set
- [ ] SSL certificate installed
- [ ] JWT secret key generated and secure
- [ ] Application properties configured
- [ ] Firewall rules updated
- [ ] Backups configured
- [ ] Monitoring setup
- [ ] Load balancer configured
- [ ] All tests passing
- [ ] Documentation updated
- [ ] Team trained on system

## Support & Troubleshooting

For issues:
1. Check logs: `tail -f logs/revcast.log`
2. Review database: `mysql -u revcast -p -D revcast_db`
3. Test endpoints: Use Swagger UI or Postman
4. Check configuration: Review application.yml
5. Review documentation: README.md, QUICKSTART.md

## Success Indicators

✅ Application started successfully
✅ Swagger UI accessible
✅ Health check returns OK
✅ Login produces JWT token
✅ APIs respond with valid data
✅ Logs show no errors
✅ Tests pass
✅ Database migrations applied

