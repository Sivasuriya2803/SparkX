package com.revcast.project.service;

import com.revcast.account.entity.Segment;
import com.revcast.account.repository.SegmentRepository;
import com.revcast.common.exception.ResourceNotFoundException;
import com.revcast.common.exception.ValidationException;
import com.revcast.project.entity.Project;
import com.revcast.project.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Project Service
 */
@Slf4j
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    /**
     * Create project
     */
    @Transactional
    public Project createProject(Project project) {
        log.info("Creating project: {}", project.getProjectName());

        Segment segment = segmentRepository.findById(project.getSegment().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Segment", "id", project.getSegment().getId()));

        if (projectRepository.existsByProjectCode(project.getProjectCode())) {
            throw new ValidationException("Project with code " + project.getProjectCode() + " already exists");
        }

        project.setSegment(segment);
        project.setIsActive(true);
        return projectRepository.save(project);
    }

    /**
     * Get project by ID
     */
    @Transactional(readOnly = true)
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));
    }

    /**
     * Get project by code
     */
    @Transactional(readOnly = true)
    public Project getProjectByCode(String code) {
        return projectRepository.findByProjectCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "code", code));
    }

    /**
     * Get projects by segment
     */
    @Transactional(readOnly = true)
    public List<Project> getProjectsBySegment(Long segmentId) {
        return projectRepository.findBySegmentId(segmentId);
    }

    /**
     * Get all active projects
     */
    @Transactional(readOnly = true)
    public List<Project> getAllActiveProjects() {
        return projectRepository.findByIsActiveTrue();
    }

    /**
     * Update project
     */
    @Transactional
    public Project updateProject(Long id, Project projectUpdate) {
        log.info("Updating project: {}", id);

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        project.setProjectName(projectUpdate.getProjectName());
        project.setStartDate(projectUpdate.getStartDate());
        project.setEndDate(projectUpdate.getEndDate());
        project.setDescription(projectUpdate.getDescription());

        return projectRepository.save(project);
    }

    /**
     * Deactivate project
     */
    @Transactional
    public Project deactivateProject(Long id) {
        log.info("Deactivating project: {}", id);

        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", id));

        project.setIsActive(false);
        return projectRepository.save(project);
    }
}

