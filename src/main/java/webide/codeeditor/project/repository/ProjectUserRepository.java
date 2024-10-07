package webide.codeeditor.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectUserRepository extends JpaRepository<ProjectUser, Long> {
    List<ProjectUser> findByUserId(Long userId);

    @Query("SELECT pu.project.id FROM ProjectUser pu WHERE pu.user.id = :userId")
    List<Long> findProjectIdsByUserId(Long userId);
}
