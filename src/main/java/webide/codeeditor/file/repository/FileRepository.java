package webide.codeeditor.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileEntity, UUID> {

    // 파일명을 기준으로 파일 엔티티를 조회하는 메서드
    Optional<FileEntity> findByFileName(String fileName);
}
