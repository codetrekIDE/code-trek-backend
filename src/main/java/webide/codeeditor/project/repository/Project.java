package webide.codeeditor.project.repository;

import jakarta.persistence.*;
import lombok.*;
import webide.codeeditor.file.repository.FileEntity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    //TODO 파일 id 연결

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProjectUser> projectUsers = new HashSet<>();

    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private FileEntity file;

    @Column
    private Timestamp created_at;

    @Column
    private Timestamp updated_at;
}
