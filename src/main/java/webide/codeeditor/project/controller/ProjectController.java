package webide.codeeditor.project.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import webide.codeeditor.project.model.request.ProjectCreateRequest;
import webide.codeeditor.project.model.request.ProjectUpdateRequest;
import webide.codeeditor.project.model.response.ProjectCreateResponse;
import webide.codeeditor.project.model.response.ProjectListResponse;
import webide.codeeditor.project.model.response.ProjectResponse;
import webide.codeeditor.project.service.ProjectService;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/create")
    public ResponseEntity<?> createProject(
            @RequestBody ProjectCreateRequest projectCreateRequest,
            @CookieValue(name = "userId", defaultValue = "1") Long userId
            ) {
        System.out.println("들어왔습니다");
        System.out.println(userId);
        System.out.println(projectCreateRequest.getTitle());
        if (userId == null) {
            return ResponseEntity.status(401).body("Error: Invalid login credentials.");
        }

        ProjectCreateResponse projectCreateResponse = projectService.createProject(userId, projectCreateRequest);

        return new ResponseEntity<>(projectCreateResponse, HttpStatus.OK);
    }

    //TODO 다시 만들 것
    @PutMapping("/update/{projectId}")
    public ResponseEntity<Void> updateProject(
            @CookieValue(name = "userId") Long userId,
            @PathVariable Long projectId,
            @RequestBody ProjectUpdateRequest projectUpdateRequest
    ) {
        projectService.updateProject(projectId, projectUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @CookieValue(name = "userId") Long userId,
            @PathVariable Long projectId
    ) {
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getProjectList(
            @CookieValue(name = "userId") Long userId
    ) {
        ProjectListResponse projectListResponse = projectService.getProjectList(userId);
        return ResponseEntity.ok(projectListResponse);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(
            @CookieValue(name = "userId") Long userId,
            @PathVariable Long projectId
    ) {
        return new ResponseEntity<ProjectResponse>(projectService.getProject(projectId), HttpStatus.OK);
    }

}
