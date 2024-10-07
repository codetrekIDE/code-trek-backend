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
            @CookieValue(name = "userId") Long userId
            ) {

        if (userId == null) {
            return ResponseEntity.status(401).body("Error: Invalid login credentials.");
        }

        projectService.createProject(userId, projectCreateRequest);
        System.out.println(userId);

        // Todo 중계 테이블 만들기

        return new ResponseEntity<ProjectCreateResponse>(HttpStatus.OK);
    }

    @PutMapping("/{userId}/update/{projectId}")
    public ResponseEntity<Void> updateProject(
            @PathVariable Long userId,
            @PathVariable Long projectId,
            @RequestBody ProjectUpdateRequest projectUpdateRequest
    ) {
        projectService.updateProject(projectId, projectUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}/delete/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long userId,
            @PathVariable Long projectId
    ) {
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<List<ProjectResponse>> getProjectList(
//            @PathVariable Long userId
//    ) {
//        projectService.getProjectList(userId);
//    }

    @GetMapping("/{userId}/get/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(
            @PathVariable Long projectId
    ) {
        return new ResponseEntity<ProjectResponse>(projectService.getProject(projectId), HttpStatus.OK);
    }

}
