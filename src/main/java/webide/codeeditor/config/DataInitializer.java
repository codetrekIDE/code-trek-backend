package webide.codeeditor.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import webide.codeeditor.file.service.FileOperationService;
import webide.codeeditor.member.domain.entity.User;
import webide.codeeditor.member.repository.UserRepository;
import webide.codeeditor.project.repository.Project;
import webide.codeeditor.project.repository.ProjectRepository;
import webide.codeeditor.project.repository.ProjectUser;
import webide.codeeditor.project.repository.ProjectUserRepository;

import java.sql.Timestamp;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository memberRepository;

    @Autowired
    private ProjectUserRepository projectUserRepository;

    @Autowired
    private FileOperationService fileOperationService;

    @Override
    public void run(String... args) throws Exception {

        for (int i = 1; i < 6; i++) {
            String loginId = "lsj" + Integer.toString(i);
            String password = "password" + Integer.toString(i);
            User member = User.builder()
                    .loginId(loginId)
                    .password(password)
                    .build();
            memberRepository.save(member);

            String name = "새 프로젝트" + Integer.toString(i);
            String description = "테스트용 프로젝트 더미 데이터입니다" + Integer.toString(i);
            Project project = Project.builder()
                    .title(name)
                    .description(description)
                    .created_at(new Timestamp(System.currentTimeMillis()))
                    .updated_at(new Timestamp(System.currentTimeMillis()))
                    .build();
            Project savedProject = projectRepository.save(project);

            ProjectUser projectUser = ProjectUser.builder()
                    .user(member)
                    .project(project)
                    .build();
            projectUserRepository.save(projectUser);

            fileOperationService.createFile("main.py", "", savedProject.getId());
        }
    }
}
