-- V1__Initial_Schema.sql
-- RevCast Database Schema
-- Created: 2026-06-06

-- Create Role table
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create User table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    role_id BIGINT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT,
    FOREIGN KEY (role_id) REFERENCES roles(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Account table
CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_name VARCHAR(255) NOT NULL UNIQUE,
    account_code VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_account_code (account_code),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Segment table
CREATE TABLE IF NOT EXISTS segments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    segment_name VARCHAR(255) NOT NULL UNIQUE,
    segment_code VARCHAR(50) NOT NULL UNIQUE,
    account_id BIGINT NOT NULL,
    description VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_segment_code (segment_code),
    INDEX idx_account_id (account_id),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Project table
CREATE TABLE IF NOT EXISTS projects (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    project_name VARCHAR(255) NOT NULL,
    project_code VARCHAR(50) NOT NULL UNIQUE,
    segment_id BIGINT NOT NULL,
    start_date DATE,
    end_date DATE,
    description VARCHAR(500),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (segment_id) REFERENCES segments(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_project_code (project_code),
    INDEX idx_segment_id (segment_id),
    INDEX idx_is_active (is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Employee table
CREATE TABLE IF NOT EXISTS employees (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_code VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15),
    date_of_birth DATE,
    date_of_joining DATE NOT NULL,
    last_working_day DATE,
    designation VARCHAR(100),
    billing_rate DECIMAL(10, 2),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_employee_code (employee_code),
    INDEX idx_email (email),
    INDEX idx_is_active (is_active),
    INDEX idx_date_of_joining (date_of_joining)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create ResourceAllocation table
CREATE TABLE IF NOT EXISTS resource_allocations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    allocation_percentage DECIMAL(5, 2) NOT NULL,
    billing_rate DECIMAL(10, 2),
    hours_per_day DECIMAL(5, 2) DEFAULT 8.0,
    allocation_status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_employee_id (employee_id),
    INDEX idx_project_id (project_id),
    INDEX idx_start_date (start_date),
    UNIQUE KEY uk_employee_project_dates (employee_id, project_id, start_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create STBO Position table (Sold To Be Offered)
CREATE TABLE IF NOT EXISTS stbo_positions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    position_code VARCHAR(50) NOT NULL UNIQUE,
    project_id BIGINT NOT NULL,
    designation VARCHAR(100),
    billing_rate DECIMAL(10, 2) NOT NULL,
    start_date DATE NOT NULL,
    expected_fill_date DATE,
    filled_employee_id BIGINT,
    status VARCHAR(50) NOT NULL,
    reason_for_backfill VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (filled_employee_id) REFERENCES employees(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_project_id (project_id),
    INDEX idx_status (status),
    INDEX idx_filled_employee_id (filled_employee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Leave Record table
CREATE TABLE IF NOT EXISTS leave_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    leave_type VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    number_of_days INT NOT NULL,
    reason VARCHAR(255),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_employee_id (employee_id),
    INDEX idx_start_date (start_date),
    INDEX idx_end_date (end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Holiday table
CREATE TABLE IF NOT EXISTS holidays (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    holiday_name VARCHAR(255) NOT NULL,
    holiday_date DATE NOT NULL,
    holiday_type VARCHAR(50),
    is_company_wide BOOLEAN DEFAULT FALSE,
    project_id BIGINT,
    description VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_holiday_date (holiday_date),
    INDEX idx_is_company_wide (is_company_wide),
    UNIQUE KEY uk_holiday_date_project (holiday_date, project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Resignation table
CREATE TABLE IF NOT EXISTS resignations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    resignation_date DATE NOT NULL,
    last_working_day DATE NOT NULL,
    reason VARCHAR(500),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_employee_id (employee_id),
    INDEX idx_last_working_day (last_working_day)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Backfill Request table
CREATE TABLE IF NOT EXISTS backfill_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    resignation_id BIGINT NOT NULL,
    stbo_position_id BIGINT,
    requested_date DATE NOT NULL,
    priority VARCHAR(50),
    status VARCHAR(50),
    backfill_employee_id BIGINT,
    backfill_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (resignation_id) REFERENCES resignations(id),
    FOREIGN KEY (stbo_position_id) REFERENCES stbo_positions(id),
    FOREIGN KEY (backfill_employee_id) REFERENCES employees(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_resignation_id (resignation_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create Forecast table
CREATE TABLE IF NOT EXISTS forecasts (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    forecast_code VARCHAR(50) NOT NULL UNIQUE,
    forecast_type VARCHAR(50) NOT NULL,
    forecast_period VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    total_revenue DECIMAL(15, 2),
    sbl_revenue DECIMAL(15, 2),
    stbo_revenue DECIMAL(15, 2),
    status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_forecast_type (forecast_type),
    INDEX idx_start_date (start_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create ForecastDetail table
CREATE TABLE IF NOT EXISTS forecast_details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    forecast_id BIGINT NOT NULL,
    allocation_id BIGINT,
    stbo_position_id BIGINT,
    employee_id BIGINT,
    project_id BIGINT,
    segment_id BIGINT,
    account_id BIGINT,
    billable_days INT,
    billable_hours DECIMAL(10, 2),
    billing_rate DECIMAL(10, 2),
    revenue DECIMAL(15, 2),
    revenue_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (forecast_id) REFERENCES forecasts(id),
    FOREIGN KEY (allocation_id) REFERENCES resource_allocations(id),
    FOREIGN KEY (stbo_position_id) REFERENCES stbo_positions(id),
    FOREIGN KEY (employee_id) REFERENCES employees(id),
    FOREIGN KEY (project_id) REFERENCES projects(id),
    FOREIGN KEY (segment_id) REFERENCES segments(id),
    FOREIGN KEY (account_id) REFERENCES accounts(id),
    INDEX idx_forecast_id (forecast_id),
    INDEX idx_employee_id (employee_id),
    INDEX idx_project_id (project_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create VarianceAnalysis table
CREATE TABLE IF NOT EXISTS variance_analysis (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    current_forecast_id BIGINT NOT NULL,
    previous_forecast_id BIGINT,
    variance_amount DECIMAL(15, 2),
    variance_percentage DECIMAL(10, 2),
    analysis_status VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_by BIGINT NOT NULL,
    updated_by BIGINT,
    FOREIGN KEY (current_forecast_id) REFERENCES forecasts(id),
    FOREIGN KEY (previous_forecast_id) REFERENCES forecasts(id),
    FOREIGN KEY (created_by) REFERENCES users(id),
    FOREIGN KEY (updated_by) REFERENCES users(id),
    INDEX idx_current_forecast_id (current_forecast_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create VarianceReason table
CREATE TABLE IF NOT EXISTS variance_reasons (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    variance_analysis_id BIGINT NOT NULL,
    reason_type VARCHAR(100) NOT NULL,
    impact_amount DECIMAL(15, 2),
    description VARCHAR(500),
    FOREIGN KEY (variance_analysis_id) REFERENCES variance_analysis(id),
    INDEX idx_variance_analysis_id (variance_analysis_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create AuditLog table
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT,
    action VARCHAR(50) NOT NULL,
    old_value LONGTEXT,
    new_value LONGTEXT,
    user_id BIGINT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_entity_type (entity_type),
    INDEX idx_entity_id (entity_id),
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default roles
INSERT IGNORE INTO roles (name, description) VALUES
('ADMIN', 'Administrator with full access'),
('PROJECT_MANAGER', 'Project Manager with project-level access'),
('DELIVERY_HEAD', 'Delivery Head with delivery-level access'),
('FINANCE', 'Finance team member for reporting'),
('VIEWER', 'Read-only access');

