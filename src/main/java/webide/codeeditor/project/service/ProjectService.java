package webide.codeeditor.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webide.codeeditor.project.model.request.ProjectCreateRequest;
import webide.codeeditor.project.model.request.ProjectUpdateRequest;
import webide.codeeditor.project.model.response.ProjectResponse;
import webide.codeeditor.project.repository.Project;
import webide.codeeditor.project.repository.ProjectRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public void createProject(Long userId, ProjectCreateRequest projectCreateRequest) {
        // 프로젝트 생성
        Project project = Project.builder()
                .name(projectCreateRequest.getName())
                .description(projectCreateRequest.getDescription())
                .created_at(new Timestamp(System.currentTimeMillis()))
                .updated_at(new Timestamp(System.currentTimeMillis()))
                .build();

        // db에 저장
        Project savedProject = projectRepository.save(project);

        //TODO 파일 생성하기
    }

    public void updateProject(Long projectId, ProjectUpdateRequest projectUpdateRequest) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);

        // TODO 미흡한 부분 수정하기
        Project project = projectOptional.get();
        project.setName(projectUpdateRequest.getName());
        project.setDescription(projectUpdateRequest.getDescription());
        project.setUpdated_at(new Timestamp((System.currentTimeMillis())));

        projectRepository.save(project);
    }

    public void deleteProject(Long projectId) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);

        Project project = projectOptional.get();
        projectRepository.delete(project);
    }


//    public List<ProjectResponse> getProjectList(Long userId) {
//
//    }

    public ProjectResponse getProject(Long projectId) {
        // project 불러오기
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        Project project = projectOptional.get();

        // projectResponse로 변경
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .created_at(project.getCreated_at())
                .updated_at(project.getUpdated_at())
                .build();

        return projectResponse;
    }
}
