package com.revcast.employee.repository;

import com.revcast.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Employee Repository
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByEmail(String email);

    List<Employee> findByIsActiveTrue();

    List<Employee> findByDateOfJoiningBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT e FROM Employee e WHERE e.lastWorkingDay IS NOT NULL AND e.lastWorkingDay <= :date")
    List<Employee> findResignedEmployeesBefore(@Param("date") LocalDate date);

    Boolean existsByEmployeeCode(String employeeCode);

    Boolean existsByEmail(String email);
}

