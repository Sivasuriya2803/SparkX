package com.revcast.resignation.repository;

import com.revcast.resignation.entity.Resignation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ResignationRepository extends JpaRepository<Resignation, Long> {
    Optional<Resignation> findByEmployeeId(Long id);
    List<Resignation> findByLastWorkingDayAfter(LocalDate lastWorkingDayAfter);
    List<Resignation> findByStatus(String status);
}