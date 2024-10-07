package webide.codeeditor.file.repository;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
// 엔티티와 DB 연동
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;    // 고유한 id (UUID)

    @Column(name = "file_name", nullable = false)
    private String fileName; // 파일 경로, 파일 이름

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private java.time.LocalDateTime createdAt;

    // 생성자 : 파일 생성시 ID는 자동 생성되고, 생성 기록도 저장
    public FileEntity(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
        this.createdAt = java.time.LocalDateTime.now(); // 파일이 생성된 시각을 저
    }
}
