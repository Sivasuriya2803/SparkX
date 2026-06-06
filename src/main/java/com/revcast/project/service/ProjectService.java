package com.revcast.project.service;

import com.revcast.account.entity.Segment;
import com.revcast.account.repository.SegmentRepository;
import com.revcast.common.exception.ResourceNotFoundException;
import com.revcast.common.exception.ValidationException;
import com.revcast.project.dto.ProjectRequest;
import com.revcast.project.dto.ProjectResponse;
import com.revcast.project.entity.Project;
import com.revcast.project.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    @Transactional
    public ProjectResponse createProject(ProjectRequest request) {
        log.info("Creating new project with code: {}", request.getProjectCode());

        if (projectRepository.existsByProjectCode(request.getProjectCode())) {
            throw new ValidationException("Project with code " + request.getProjectCode() + " already exists.");
        }

        Segment segment = segmentRepository.findById(request.getSegmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Segment not found with ID: " + request.getSegmentId()));

        Project project = Project.builder()
                .projectName(request.getProjectName())
                .projectCode(request.getProjectCode())
                .segment(segment)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .description(request.getDescription())
                .isActive(request.getIsActive())
                .build();

        Project savedProject = projectRepository.save(project);
        log.info("Project created successfully with ID: {}", savedProject.getId());
        return mapToProjectResponse(savedProject);
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProjectById(Long id) {
        log.info("Fetching project with ID: {}", id);
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));
        return mapToProjectResponse(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAllProjects() {
        log.info("Fetching all projects");
        return projectRepository.findAll().stream()
                .map(this::mapToProjectResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProjectResponse updateProject(Long id, ProjectRequest request) {
        log.info("Received request to update project with ID: {}", id);
        Project existingProject = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with ID: " + id));

        if (!existingProject.getProjectCode().equals(request.getProjectCode()) &&
                projectRepository.existsByProjectCode(request.getProjectCode())) {
            throw new ValidationException("Project with code " + request.getProjectCode() + " already exists.");
        }

        Segment segment = segmentRepository.findById(request.getSegmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Segment not found with ID: " + request.getSegmentId()));

        existingProject.setProjectName(request.getProjectName());
        existingProject.setProjectCode(request.getProjectCode());
        existingProject.setSegment(segment);
        existingProject.setStartDate(request.getStartDate());
        existingProject.setEndDate(request.getEndDate());
        existingProject.setDescription(request.getDescription());
        existingProject.setIsActive(request.getIsActive());

        Project updatedProject = projectRepository.save(existingProject);
        log.info("Project updated successfully with ID: {}", updatedProject.getId());
        return mapToProjectResponse(updatedProject);
    }

    @Transactional
    public void deleteProject(Long id) {
        log.info("Received request to delete project with ID: {}", id);
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
        log.info("Project deleted successfully with ID: {}", id);
    }

    private ProjectResponse mapToProjectResponse(Project project) {
        Long segmentId = null;
        String segmentName = null;

        if (project.getSegment() != null) {
            segmentId = project.getSegment().getId();
            segmentName = project.getSegment().getSegmentName(); // Corrected method call
        }

        return ProjectResponse.builder()
                .id(project.getId())
                .projectName(project.getProjectName())
                .projectCode(project.getProjectCode())
                .segmentId(segmentId)
                .segmentName(segmentName)
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .description(project.getDescription())
                .isActive(project.getIsActive())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
