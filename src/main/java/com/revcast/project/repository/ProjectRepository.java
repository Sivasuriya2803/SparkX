package com.revcast.project.repository;

import com.revcast.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Project Repository
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByProjectCode(String projectCode);

    List<Project> findBySegmentId(Long segmentId);

    List<Project> findByIsActiveTrue();

    Boolean existsByProjectCode(String projectCode);
}

