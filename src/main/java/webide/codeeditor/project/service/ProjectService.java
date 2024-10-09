package webide.codeeditor.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webide.codeeditor.file.repository.FileEntity;
import webide.codeeditor.file.repository.FileRepository;
import webide.codeeditor.member.domain.entity.User;
import webide.codeeditor.member.repository.UserRepository;
import webide.codeeditor.project.model.request.ProjectCreateRequest;
import webide.codeeditor.project.model.request.ProjectSaveCodeRequest;
import webide.codeeditor.project.model.request.ProjectUpdateRequest;
import webide.codeeditor.project.model.response.ProjectCreateResponse;
import webide.codeeditor.project.model.response.ProjectGetResponse;
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
    private final FileRepository fileRepository;

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


    public List<ProjectListResponse> getProjectList(Long userId) {

        List<Long> projectIdList = projectUserRepository.findProjectIdsByUserId(userId);
        List<Project> projects = projectRepository.findAllById(projectIdList);

        List<ProjectListResponse> responseList = projects.stream()
                .map(project -> ProjectListResponse.builder()
                        .id(project.getId())
                        .title(project.getTitle())
                        .updated_at(project.getUpdated_at())
                        .build())
                .collect(Collectors.toList());

        return responseList;
    }

    public ProjectGetResponse getProject(Long projectId) {
        // project 불러오기
        Optional<Project> projectOptional = projectRepository.findById(projectId);
        Project project = projectOptional.get();

        Optional<FileEntity> fileOptional = fileRepository.findByProjectId(projectId);
        FileEntity file = fileOptional.get();

        // projectResponse로 변경
        ProjectGetResponse projectGetResponse = ProjectGetResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .description(project.getDescription())
                .created_at(project.getCreated_at())
                .updated_at(project.getUpdated_at())
                .code(file.getContent())
                .build();

        return projectGetResponse;
    }

    public void saveProjectCode(ProjectSaveCodeRequest projectSaveCodeRequest) {

        Long projectId = projectSaveCodeRequest.getProjectId();
        String code = projectSaveCodeRequest.getCode();

        Optional<FileEntity> fileOptional = fileRepository.findByProjectId(projectId);
        FileEntity file = fileOptional.get();
        file.setContent(code);
        fileRepository.save(file);
    }
}
