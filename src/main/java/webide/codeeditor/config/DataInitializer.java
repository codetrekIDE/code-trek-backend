package webide.codeeditor.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import webide.codeeditor.membertemp.User;
import webide.codeeditor.membertemp.UserRepository;
import webide.codeeditor.project.repository.Project;
import webide.codeeditor.project.repository.ProjectRepository;

import java.sql.Timestamp;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {

        for (int i = 1; i < 6; i++) {
            String name = "사용자" + Integer.toString(i);
            String email = "lsj" + Integer.toString(i) + "@gmail.com";
            String password = "password" + Integer.toString(i);
            User member = User.builder()
                    .name(name)
                    .email(email)
                    .password(password)
                    .build();
            memberRepository.save(member);
        }

        for (int i = 1; i < 6; i++) {
            String name = "새 프로젝트" + Integer.toString(i);
            String description = "테스트용 프로젝트 더미 데이터입니다" + Integer.toString(i);
            Project project = Project.builder()
                    .name(name)
                    .description(description)
                    .created_at(new Timestamp(System.currentTimeMillis()))
                    .updated_at(new Timestamp(System.currentTimeMillis()))
                    .build();
            projectRepository.save(project);
        }

    }
}
