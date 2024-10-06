package webide.codeeditor.project.repository;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

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
    private String name;

    @Column
    private String description;

    //TODO 파일 id 연결

    @Column
    private Timestamp created_at;

    @Column
    private Timestamp updated_at;
}
