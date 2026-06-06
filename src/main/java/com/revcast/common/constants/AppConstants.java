package com.revcast.common.constants;

/**
 * Application-level constants
 */
public class AppConstants {

    // Security
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    public static final String JWT_CLAIM_SUB = "sub";

    // Roles
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_PROJECT_MANAGER = "PROJECT_MANAGER";
    public static final String ROLE_DELIVERY_HEAD = "DELIVERY_HEAD";
    public static final String ROLE_FINANCE = "FINANCE";
    public static final String ROLE_VIEWER = "VIEWER";

    // Allocation Status
    public static final String ALLOCATION_STATUS_ACTIVE = "ACTIVE";
    public static final String ALLOCATION_STATUS_INACTIVE = "INACTIVE";
    public static final String ALLOCATION_STATUS_ON_HOLD = "ON_HOLD";

    // STBO Status
    public static final String STBO_STATUS_OPEN = "OPEN";
    public static final String STBO_STATUS_FILLED = "FILLED";
    public static final String STBO_STATUS_CLOSED = "CLOSED";
    public static final String STBO_STATUS_ON_HOLD = "ON_HOLD";

    // Leave Status
    public static final String LEAVE_STATUS_PENDING = "PENDING";
    public static final String LEAVE_STATUS_APPROVED = "APPROVED";
    public static final String LEAVE_STATUS_REJECTED = "REJECTED";

    // Resignation Status
    public static final String RESIGNATION_STATUS_PENDING = "PENDING";
    public static final String RESIGNATION_STATUS_APPROVED = "APPROVED";
    public static final String RESIGNATION_STATUS_REJECTED = "REJECTED";
    public static final String RESIGNATION_STATUS_PROCESSED = "PROCESSED";

    // Backfill Status
    public static final String BACKFILL_STATUS_OPEN = "OPEN";
    public static final String BACKFILL_STATUS_IN_PROGRESS = "IN_PROGRESS";
    public static final String BACKFILL_STATUS_FILLED = "FILLED";
    public static final String BACKFILL_STATUS_CLOSED = "CLOSED";

    // Forecast Type
    public static final String FORECAST_TYPE_WEEKLY = "WEEKLY";
    public static final String FORECAST_TYPE_MONTHLY = "MONTHLY";
    public static final String FORECAST_TYPE_QUARTERLY = "QUARTERLY";

    // Forecast Status
    public static final String FORECAST_STATUS_DRAFT = "DRAFT";
    public static final String FORECAST_STATUS_FINALIZED = "FINALIZED";
    public static final String FORECAST_STATUS_PUBLISHED = "PUBLISHED";
    public static final String FORECAST_STATUS_ARCHIVED = "ARCHIVED";

    // Revenue Type
    public static final String REVENUE_TYPE_SBL = "SBL";
    public static final String REVENUE_TYPE_STBO = "STBO";

    // Leave Type
    public static final String LEAVE_TYPE_SICK = "SICK";
    public static final String LEAVE_TYPE_CASUAL = "CASUAL";
    public static final String LEAVE_TYPE_EARNED = "EARNED";
    public static final String LEAVE_TYPE_UNPAID = "UNPAID";

    // Holiday Type
    public static final String HOLIDAY_TYPE_COMPANY = "COMPANY";
    public static final String HOLIDAY_TYPE_CLIENT = "CLIENT";
    public static final String HOLIDAY_TYPE_REGIONAL = "REGIONAL";

    // Variance Reasons
    public static final String VARIANCE_REASON_EMPLOYEE_JOINED = "Employee Joined";
    public static final String VARIANCE_REASON_EMPLOYEE_RELEASED = "Employee Released";
    public static final String VARIANCE_REASON_EMPLOYEE_RESIGNED = "Employee Resigned";
    public static final String VARIANCE_REASON_EMPLOYEE_REALLOCATED = "Employee Reallocated";
    public static final String VARIANCE_REASON_LEAVE_ADDED = "Leave Added";
    public static final String VARIANCE_REASON_HOLIDAY_ADDED = "Holiday Added";
    public static final String VARIANCE_REASON_BILLING_RATE_CHANGED = "Billing Rate Changed";
    public static final String VARIANCE_REASON_BACKFILL_CREATED = "Backfill Created";

    // Audit Actions
    public static final String AUDIT_ACTION_CREATE = "CREATE";
    public static final String AUDIT_ACTION_UPDATE = "UPDATE";
    public static final String AUDIT_ACTION_DELETE = "DELETE";

    // Working hours
    public static final Integer HOURS_PER_DAY = 8;
}

