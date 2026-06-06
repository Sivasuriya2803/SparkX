package com.revcast.project.controller;

import com.revcast.common.response.ApiResponse;
import com.revcast.project.dto.ProjectRequest;
import com.revcast.project.dto.ProjectResponse;
import com.revcast.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/projects")
@Tag(name = "Project Management", description = "Endpoints for managing projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    @Operation(summary = "Create a new project", description = "Creates a new project and associates it with a segment")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(@Valid @RequestBody ProjectRequest request) {
        log.info("Received request to create project: {}", request.getProjectName());
        ProjectResponse response = projectService.createProject(request);
        return new ResponseEntity<>(ApiResponse.success(response, "Project created successfully"), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieves a single project by its unique identifier")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProjectById(@PathVariable Long id) {
        log.info("Received request to get project with ID: {}", id);
        ProjectResponse response = projectService.getProjectById(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Project fetched successfully"));
    }

    @GetMapping
    @Operation(summary = "Get all projects", description = "Retrieves a list of all projects")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getAllProjects() {
        log.info("Received request to get all projects");
        List<ProjectResponse> response = projectService.getAllProjects();
        return ResponseEntity.ok(ApiResponse.success(response, "Projects fetched successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project", description = "Updates the details of an existing project by ID")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        log.info("Received request to update project with ID: {}", id);
        ProjectResponse response = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Project updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project", description = "Deletes a project by its unique identifier")
    public ResponseEntity<ApiResponse<Void>> deleteProject(@PathVariable Long id) {
        log.info("Received request to delete project with ID: {}", id);
        projectService.deleteProject(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Project deleted successfully"));
    }
}
