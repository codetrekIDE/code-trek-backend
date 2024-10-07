package webide.codeeditor.project.repository;

import jakarta.persistence.*;
import lombok.*;
import webide.codeeditor.member.domain.entity.User;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProjectUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name="PROJECT_ID")
    private Project project;
}
