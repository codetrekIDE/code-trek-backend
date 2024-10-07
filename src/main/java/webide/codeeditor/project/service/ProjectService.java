package webide.codeeditor.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webide.codeeditor.member.domain.entity.User;
import webide.codeeditor.member.repository.UserRepository;
import webide.codeeditor.project.model.request.ProjectCreateRequest;
import webide.codeeditor.project.model.request.ProjectUpdateRequest;
import webide.codeeditor.project.model.response.ProjectCreateResponse;
import webide.codeeditor.project.model.response.ProjectListResponse;
import webide.codeeditor.project.model.response.ProjectResponse;
import webide.codeeditor.project.repository.Project;
import webide.codeeditor.project.repository.ProjectRepository;
import webide.codeeditor.project.repository.ProjectUser;
import webide.codeeditor.project.repository.ProjectUserRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;
    private final UserRepository userRepository;

    public ProjectCreateResponse createProject(
            Long userId,
            ProjectCreateRequest projectCreateRequest
    ) {
        // 프로젝트 생성
        Project project = Project.builder()
                .title(projectCreateRequest.getTitle())
                .description(projectCreateRequest.getDescription())
                .created_at(new Timestamp(System.currentTimeMillis()))
//                .created_at(projectCreateRequest.getCreated_at())
                .updated_at(new Timestamp(System.currentTimeMillis()))
                .build();
        // db에 저장
        Project savedProject = projectRepository.save(project);

        // user 찾기
        Optional<User> userOptional = userRepository.findById(userId);

        // 중계테이블에 설정
        ProjectUser projectUser = ProjectUser.builder()
                .user(userOptional.get())
                .project(project)
                .build();

        projectUserRepository.save(projectUser);

        //TODO 파일 생성하기

        //ProjectCreateResponse로 변환
        ProjectCreateResponse projectCreateResponse = ProjectCreateResponse.builder()
                .projectId(project.getId())
                .build();

        return projectCreateResponse;
    }

    public void updateProject(Long projectId, ProjectUpdateRequest projectUpdateRequest) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);

        // TODO 미흡한 부분 수정하기
        Project project = projectOptional.get();
        project.setTitle(projectUpdateRequest.getTitle());
        project.setDescription(projectUpdateRequest.getDescription());
        project.setUpdated_at(new Timestamp((System.currentTimeMillis())));

        projectRepository.save(project);
    }

    public void deleteProject(Long projectId) {
        Optional<Project> projectOptional = projectRepository.findById(projectId);

        // 중계 테이블도 삭제하나?

        Project project = projectOptional.get();
        System.out.println(project.getId());
        projectRepository.deleteById(project.getId());
    }


    public ProjectListResponse getProjectList(Long userId) {

        List<Long> projectIdList = projectUserRepository.findProjectIdsByUserId(userId);
        ProjectListResponse response = ProjectListResponse.builder()
                .projectIdList(projectIdList)
                .build();

        return response;
    }

    public ProjectResponse getProject(Long projectId) {
        // project 불러오기
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        Project project = projectOptional.get();

        // projectResponse로 변경
        ProjectResponse projectResponse = ProjectResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .created_at(project.getCreated_at())
                .updated_at(project.getUpdated_at())
                .build();

        return projectResponse;
    }
}
