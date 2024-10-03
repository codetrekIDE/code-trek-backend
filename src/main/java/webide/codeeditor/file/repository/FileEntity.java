package webide.codeeditor.file.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
// 엔티티와 DB 연동
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    public FileEntity(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        this.createdAt = java.time.LocalDateTime.now();
    }
}
